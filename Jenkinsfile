@Grapes(
    @Grab(group='org.jsoup', module='jsoup', version='1.6.2')
)
import org.jsoup.*
import org.jsoup.nodes.*
import org.jsoup.select.*
import java.util.*;
import java.text.SimpleDateFormat
//import hudson.plugins.git.*
import hudson.Util;

node (label: 'slave1') {

def totalpassed = 0
def totalfailed = 0
def totalnontexecuted = 0
def totaltests = 0
def totalbugs = 0
def date = new Date()
def date_format=new SimpleDateFormat("EEE dd MMM HH:mm:ss z")
def build_date =date_format.format(date) 

def gitBranch='master'
def imageName= 'restassured'
List testArray = new ArrayList<Map<String,String>>();

    withMaven(maven:'maven') {

  env.gitBranch=gitBranch
  env.imageName = imageName
	  env.Mode="${params.modes}"

try{ 
        stage('Checkout') {

            git url: 'https://github.com/reshmigu/AtoBe_Linux_26-08-19.git', credentialsId: 'master', branch: 'master'

        }



		stage('Build') {

		    sh 'mvn package shade:shade'

            def pom = readMavenPom file:'pom.xml'

            env.version = pom.version

        }



        stage('Image') {

                sh 'docker stop restassured || true && docker rm restassured || true'

                cmd = "docker rmi restassured:${env.version} || true"

                sh cmd

                docker.build "restassured:${env.version}"

        }



        stage ('Run') {

		

			print "${params}"

			

			if ("${params.modes}" == "DRY_RUN") {

       			 sh "docker run -p 8081:8081 -h restassured --name restassured --net host -m=500m restassured:${env.version} DRY_RUN"

      	     }

      	     else if("${params.modes}" == "RUN") {

	  	 	 	 sh "docker run -p 8081:8081 -h restassured --name restassured --net host -m=500m restassured:${env.version} RUN"

      	     }

      	     else if("${params.modes}" == "FULL_RUN") {

	  	 		 sh "docker run -p 8081:8081 -h restassured --name restassured --net host -m=500m restassured:${env.version} FULL_RUN"

      	     }

            

			sh "docker cp restassured:/test-output ."

	
	print "artifacts creation"	
// Archive the build output artifacts.
  if ("${params.modes}" == "DRY_RUN") 
    archiveArtifacts artifacts: 'test-output/emailable-report.html'
	else
	archiveArtifacts artifacts: 'test-output/*report.html'
	
    print "artifacts creation ends"	
 
		

	//def currentDir = new File("").getAbsolutePath() //for windows
	pathCommand= pwd	// for linux
	def currentDir = "$pathCommand";// for linux
	def filename = 'emailable-report.html'
	if ("${params.modes}" == "FULL_RUN")
			filename = 'xray_report.html'
	print "${currentDir}/jobs/${env.JOB_NAME}/builds/${env.BUILD_NUMBER}/archive/test-output/${filename}"
	def file1 = new File("${currentDir}/jobs/${env.JOB_NAME}/builds/${env.BUILD_NUMBER}/archive/test-output/${filename}")	
		print "file exists = ${file1.exists()}"
		
		//file operation starts
		  
      
print "masterPage ${file1}"
Document doc = Jsoup.parse(file1, "UTF-8");


print "totalpassed ${totalpassed}" 
 	
	  if ("${params.modes}" != "FULL_RUN") {
    for (Element table : doc.getElementById("summary")) {
       // print "table found"
        for (Element row : table.select("tr.passedeven")) {
            Elements tds = row.select("td");  
			Map<String,String> map1  = new HashMap<>();
			
            totalpassed=totalpassed+1			
			if (tds.size() == 4) {  				
			    map1.put("name", tds.get(1).text());
            }
            else if (tds.size() == 3) {                
               // print "doc ${tds.get(0).text()}"			
				map1.put("name", tds.get(0).text()); 
            }
		
		    map1.put("url", "${env.BUILD_URL}/console"); 
		    map1.put("flag", "1"); 
			 map1.put("bug_id", ""); 
			testArray.add(map1);
        }
		for (Element row : table.select("tr.failedeven")) {
            Elements tds = row.select("td");  
			Map<String,String> map1  = new HashMap<>();
            totalfailed=totalfailed+1			
			if (tds.size() == 4) {    
				map1.put("name", tds.get(1).text());            
                //print "test: ${tds.get(1).text()}"
            }
            else if (tds.size() == 3) {     
				map1.put("name", tds.get(0).text()); 			
               // print "test: ${tds.get(0).text()} from build"
            }
			  map1.put("url", "${env.BUILD_URL}/console"); 
		    map1.put("flag", "2");
			map1.put("bug_id", "0"); 
			testArray.add(map1);
        }
    }
  }
  else  if ("${params.modes}" == "FULL_RUN")
  {
	
//for loop starts

    	for (Element table : doc.select("table")[4]) {
       // print "table found in else part"
        for (Element row : table.select("tr")) {
		// print "*Row found in else part"
		if(row != table.select("tr").first()){
            Elements tds = row.select("td");  
			//print row
			Map<String,String> map1  = new HashMap<>();
			           		
			 if (tds.size() == 3) {                
               // print "doc ${tds.get(2).text()}"	
				map1.put("name", tds.get(0).text()); 
				def status =tds.get(1).text()
				if(status == 'PASS')
                   {
				    map1.put("flag", "1");
				    totalpassed = totalpassed+1	
				   }
				   
                 else
				 {
				    map1.put("flag", "2");
					totalfailed = totalfailed+1
					totalbugs = totalbugs+1
					Elements links = tds.get(2).select("a[href]");
					if(links.size()== 1)
					  map1.put("bug_url", links[0].attr("href")); 
			     }
				   
				map1.put("bug_id", tds.get(2).text()); 
            }
		
		    map1.put("url", "${env.BUILD_URL}/console"); 	   

			testArray.add(map1);
	     }
        }

      }		

	//end of for loop

  }
	print "total passed ${totalpassed}"
	print "total failed ${totalfailed}"
//	print "testArray ${testArray.get(0)}"
		//file operation ends
	          	  
	   	
      
	}
	}catch(Exception){ 
	 currentBuild.result = 'FAILURE'
	}
		

		

        }		


	
		stage('mail'){		    
			env.totalpassed=totalpassed
            env.totalfailed=totalfailed
			env.totalnontexecuted= totalnontexecuted
			env.totaltests=totalpassed + totalfailed + totalnontexecuted
			env.totalbugs = totalbugs
			env.build_date = build_date.toString()
			env.build_result = currentBuild.currentResult
			env.build_duration =  Util.getTimeSpanString(System.currentTimeMillis() - currentBuild.startTimeInMillis)
			print "build_duration: ${env.build_duration}"
			//print "change sets: ${currentBuild.changeSets.items}"  
		    
		 def config = [:]
	//def subject = config.subject ? config.subject : "${env.JOB_NAME} - Build #${env.BUILD_NUMBER} - ${currentBuild.currentResult}!"
	def subject = config.subject ? config.subject : "${env.JOB_NAME} Build Test Report - ${currentBuild.currentResult}!"
       subject="A-to-Be CI Execution Report"
        // Attach buildlog when the build is not successfull
        def attachLog = (config.attachLog != null) ? config.attachLog : (currentBuild.currentResult != "SUCCESS")
//	 def content = '${JELLY_SCRIPT,template="managed:Jelly2"}'
		 //def content = '${SCRIPT,template="managed:Tpm-Email-Template"}'

		 def content=createTemplate(env,testArray)
		 
         env.ForEmailPlugin = env.WORKSPACE
        emailext mimeType: 'text/html',
	attachLog :true,
	compressLog : true,
      //  body: '${FILE, path="test-output/emailable-report.html"}',
		
	body:content,		
        subject: subject,
        to: 'reshmi.g@thinkpalm.com,dhananjaya.k@thinkpalm.com'
		
		 }

    }



//function to create template
def createTemplate(env,  testArray) {
    def sb = new StringBuilder()
	sb.append '<!doctype html><html lang="en">'

sb.append '<head> <!-- Required meta tags --> <meta charset="utf-8"> <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"> <title>A-to-Be</title> </head>'
sb.append '<body style="font-family: Arial; line-height: 1.4;font-size:10px"><table style="width:600px;font-family: Arial;font-size: 12px;" align="center"><tr><td><div style="width: 600px;margin: auto; font-family: Arial;">'

//images starts

sb.append  '<div style="padding-left:25px;"> <img src="data:image/jpeg;base64,/9j/4QAYRXhpZgAASUkqAAgAAAAAAAAAAAAAAP/sABFEdWNreQABAAQAAAA8AAD/4QMvaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLwA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/PiA8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIiB4OnhtcHRrPSJBZG9iZSBYTVAgQ29yZSA1LjYtYzE0MiA3OS4xNjA5MjQsIDIwMTcvMDcvMTMtMDE6MDY6MzkgICAgICAgICI+IDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+IDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDQyAyMDE4IChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpENERCNEQ2NEM3RjYxMUU5QTQ0MDg5NkMzQjgzNTM5NyIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpENERCNEQ2NUM3RjYxMUU5QTQ0MDg5NkMzQjgzNTM5NyI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkQ0REI0RDYyQzdGNjExRTlBNDQwODk2QzNCODM1Mzk3IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkQ0REI0RDYzQzdGNjExRTlBNDQwODk2QzNCODM1Mzk3Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+/+4ADkFkb2JlAGTAAAAAAf/bAIQABgQEBAUEBgUFBgkGBQYJ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" ></div> <div>'
sb.append  '<div> <img src="data:image/jpeg;base64,/9j/4QAYRXhpZgAASUkqAAgAAAAAAAAAAAAAAP/sABFEdWNreQABAAQAAAA8AAD/4QMvaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLwA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/PiA8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIiB4OnhtcHRrPSJBZG9iZSBYTVAgQ29yZSA1LjYtYzE0MiA3OS4xNjA5MjQsIDIwMTcvMDcvMTMtMDE6MDY6MzkgICAgICAgICI+IDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+IDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDQyAyMDE4IChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpBQUJFQjQwOUM3RjYxMUU5QTZEOEI4RkRCMEY0RTlBNSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpBQUJFQjQwQUM3RjYxMUU5QTZEOEI4RkRCMEY0RTlBNSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkFBQkVCNDA3QzdGNjExRTlBNkQ4QjhGREIwRjRFOUE1IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkFBQkVCNDA4QzdGNjExRTlBNkQ4QjhGREIwRjRFOUE1Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+/+4ADkFkb2JlAGTAAAAAAf/bAIQABgQEBAUEBgUFBgkGBQYJ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" style="width: 100%"></div>'
//images ends

   //heading data starts
    sb.append '<h3 style="text-align: center; color: #0056ab;font-size:14px">A-to-Be CI Execution Report</h3>'
   
  sb.append '<table style="width: 100%;font-family: Arial;font-size: 12px;"> <tr> <td style="width: 50%">'
  
  sb.append '<table style="font-family: Arial;font-size: 12px; padding-left:0" cellpadding="3">'
  
  //BRANCH_NAME
  sb.append ' <tr> <th style="text-align: left;font-family: Arial;">Branch</th> <td style="color:#666">'
  sb.append env.gitBranch
   sb.append '</td></tr>'
   
    //image  
  sb.append ' <tr> <th style="text-align: left">Image</th> <td style="color:#666">'
  sb.append env.imageName
  sb.append '</td></tr>'
  
   //version  
  sb.append ' <tr> <th style="text-align: left">Image Version</th> <td style="color:#666">'
  sb.append env.version
  sb.append '</td></tr>'
    
   //Execution Mode  
  sb.append ' <tr> <th style="text-align: left">Execution Mode</th> <td style="color:#666">'
  sb.append env.Mode
  sb.append '</td></tr>'  
  
  sb.append '</table> </td> <td style="width: 50%" align="right">'  
  sb.append '<table style="font-family: Arial;;font-size: 12px;"  cellpadding="3">'
  
  //build Id
  sb.append ' <tr> <th style="text-align: left">Build Id</th> <td style="color:#666">'
  sb.append('<a href="'+env.BUILD_URL+'"'+' style="color:#666"><b>#'+env.BUILD_NUMBER+'</b></a>') 
   sb.append '</td></tr>'
   
     //build_result = Build Status
  sb.append ' <tr> <th style="text-align: left"> Build Status</th> <td style="color:#666">'
  sb.append env.build_result
   sb.append '</td></tr>'
   
   //build_duration
  sb.append ' <tr> <th style="text-align: left">Test Duration</th> <td style="color:#666">'
  sb.append env.build_duration
   sb.append '</td></tr>'
   
     //build_date
  sb.append '<tr> <th style="text-align: left">Build Date</th> <td style="color:#666">'
  sb.append env.build_date
  sb.append '</td></tr>'
  
   sb.append '</table></td> </tr> </table>'

  //heading data ends
    
  sb.append '<h3 style=" color: #0056ab; margin-top: 20px;font-size:14px">Execution Summary</h3>'
  
  // Execution-Summary starts 
  sb.append '<table style="width: 100%; text-align: center;font-family: Arial;font-size: 12px;" cellpadding="3">'  
  
  sb.append '<tr style="text-transform: uppercase; font-size: 11px;"> <th style="border-bottom: 1px solid #eee; width: 108px">Total Test case </th> <th style="border-bottom: 1px solid #eee">Passed </th> <th style="border-bottom: 1px solid #eee">Failed </th> <th style="border-bottom: 1px solid #eee; width: 108px"> Bugs Reported</th> </tr>'
  sb.append '<tr>'  
 sb.append '<tr>'   
   sb.append '<td style="border-bottom: 1px solid #eee;">' + env.totaltests + '</td>'
   sb.append '<td style="border-bottom: 1px solid #eee;color:#12a107">' + env.totalpassed + '</td>'
    sb.append '<td style="border-bottom: 1px solid #eee;color:#dd5050">' + env.totalfailed + '</td>'
    sb.append '<td style="border-bottom: 1px solid #eee;color:#dd5050">' + env.totalbugs + '</td>'	
sb.append '</tr></table>'	
// Execution-Summary ends

sb.append '<h3 style=" color: #0056ab; margin-top: 20px;font-size:14px">Test Case Execution Status</h3>'


//Test Case Execution Status

def StatusHeaddingText ='TEST CASE NAME'
if(env.Mode == 'FULL_RUN')
	StatusHeaddingText = 'TEST CASE ID'

sb.append ' <table style="width: 100%;font-family: Arial; text-align: left;font-size: 12px;" cellpadding="3"> <tr style="text-transform: uppercase; font-size: 11px;font-family: Arial;"> <th style="border-bottom: 1px solid #eee;width: 106px;text-align:left">'+ StatusHeaddingText +'</th> <th style="border-bottom: 1px solid #eee; text-align: center">Status </th> <th style="border-bottom: 1px solid #eee; width: 108px">Bugs Created </th> </tr>'


   
   //for each loop starts
   
    for (ArrayList<Map<String,String>> item : testArray) {  
	if(item.flag == "1"){
	
	sb.append '<tr><td style="border-bottom: 1px solid #eee;">' + item.name + '</td>'
	sb.append '<td style="border-bottom: 1px solid #eee;color:#12a107; text-align: center">'
	sb.append('<a href="'+item.url+'"'+' style="color:#12a107;">PASS</a>');	
	sb.append '</td><td style="border-bottom: 1px solid #eee;color:#12a107; text-align: center">'+ item.bug_id + '</td></tr>'
	
	}  
		if(item.flag == "2"){		 
		 	sb.append '<tr><td style="border-bottom: 1px solid #eee;">' + item.name + '</td>'
	sb.append '<td style="border-bottom: 1px solid #eee;color:#dd5050; text-align: center">'
	sb.append('<a href="'+item.url+'"'+' style="color:#dd5050;">FAIL</a>');	
	sb.append '</td><td style="border-bottom: 1px solid #eee;color:#12a107; text-align: center">'
	if(item.bug_url != null)
	    sb.append('<a href="'+item.bug_url+'"'+' style="color:#dd5050;">'+item.bug_id+'</a>' + '</td></tr>')
	   else
	    sb.append '</td><td style="border-bottom: 1px solid #eee;color:#12a107; text-align: center">'+ item.bug_id + '</td></tr>'
	
	}  
	
}   
  //for each loop ends

	if(testArray.size == 0)
       sb.append  '<tr><td colspan="3" style="border:1px solid black">No details available to show</td></tr>'
	
	sb.append ('</table></div></td></tr></table></body></html>')
	return sb.toString()
}
