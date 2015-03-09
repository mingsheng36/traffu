'''
Created on 9 Mar 2015

@author: Tan Ming Sheng
'''
from django.http import HttpResponse
import urllib2
from lxml import html
from models import News, Ids
import sys, requests, json

def index(request):
    if request.method == 'GET':
        return HttpResponse("Welcome to Traffu!")

'''
To get new updates
''' 
def refresh(request):
    if request.method == 'GET':
        try:
            #url = "http://www.onemotoring.com.sg/publish/onemotoring/en/on_the_roads/traffic_news.main.step1.html?viewType=RoadName&filterBy=All+Roads&sortBy=1"
            #url = "http://www.onemotoring.com.sg/publish/onemotoring/en/on_the_roads/traffic_news.main.step1.html?viewType=RoadName&filterBy=All+Roads&sortBy=All+Types"
            url =  "http://www.onemotoring.com.sg/publish/onemotoring/en/on_the_roads/traffic_news.main.step1.html?viewType=RoadName&filterBy=Expressways&sortBy=8"
            req = urllib2.Request(url, headers={'User-Agent': 'Mozilla/5.0'})
            res = urllib2.urlopen(req)
            news = html.fromstring(res.read()).xpath("/html/body/div[3]/div[3]/div[5]/div/div[3]/div[1]/div[1]/form/div/div[3]/div/div[1]/div/div[3]/p/text()")
            # new updates
            print 
            if News.objects.filter(news=news).count() == 0:
                
                # delete old news and save the fresh news in database for checking whether there is any updates
                News.objects.all().delete()
                News(news=news).save()
                
                # get all the registration_ids from database
                rids = Ids.objects.values_list('ids', flat=True)
                formatted_rids = []
                for rid in rids:
                    formatted_rids.append(str(rid))
                
                # put the news in <key, value> pair    
                news_dict = {}
                for i, values in enumerate(news):
                    news_dict[i] = values
                
                print "Sending data to:" + str(formatted_rids)
                print "Update size of data: " + str(sys.getsizeof(news)) + "\n" + str(news_dict)
                
                #send google notification
                google_url = 'https://android.googleapis.com/gcm/send'
                headers = {'content-type': 'application/json', 'Authorization':'key=AIzaSyCVvLAiWbVTK8yxQjbt73xdkj0vOZ2hlhQ'}
                payload = {'registration_ids': formatted_rids, 'data': news_dict}
                response_from_google = requests.post(google_url, data=json.dumps(payload), headers=headers)
                print response_from_google.text
                formatted_news = ""
                for n in news:
                    formatted_news += n + "<br/>"
                return HttpResponse("Size of data (must be < 4000 bytes): " + str(sys.getsizeof(news)) + " bytes<br/>" + formatted_news)
            
            # no updates
            else:
                return HttpResponse("No Updates")
            
        except Exception as err:
            print err

'''
To get registration ids from android app
''' 
def register(request):
    if request.method == 'GET':
        try:
            ids = request.GET['rid']
            print ids
            Ids.objects.get(ids=ids)
            return HttpResponse("fail")
        except Exception:
            Ids(ids=ids).save()
            return HttpResponse("success")
            