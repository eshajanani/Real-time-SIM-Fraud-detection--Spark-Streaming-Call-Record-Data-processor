package azureSender;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import org.joda.time.DateTime;

import azureSender.DataGenerator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.servicebus.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.ServiceBusException;

public class Send
{
	static int id =100;
	static int id1 =200;
	static int flag = 0;
	static int fraudcount =0;


	public static void main(String[] args) 
			throws ServiceBusException, ExecutionException, InterruptedException, IOException
	{
		
	        final String namespaceName = "ds-bi-dw-poc-ns";
	        final String eventHubName = "dsg_hdinsight_webinar1";
	        final String sasKeyName = "Webinar1";
	        final String sasKey = "NuLd2F53r2NZHIuj3IjdQMwF1KMH/NK1QVrFeIdZcuQ=";
	        
	        String connectionString = "Endpoint=sb://ds-bi-dw-poc-ns.servicebus.windows.net/;SharedAccessKeyName=Webinar1;SharedAccessKey=NuLd2F53r2NZHIuj3IjdQMwF1KMH/NK1QVrFeIdZcuQ=;EntityPath=dsg_hdinsight_webinar1";
	        ConnectionStringBuilder eventHubConnectionString = new ConnectionStringBuilder(connectionString);

		Gson gson = new GsonBuilder().create();
		EventHubClient sender = EventHubClient.createFromConnectionString(eventHubConnectionString.toString()).get();
		int count =0;
		 
		while (true)
		{
			Random rn = new Random();

		/*	DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			Timer timer = new Timer();
			
			timer.schedule(new MyTimeTask(), period); */
			count = count+1;
			System.out.println(" **************** COUNT :" +count +"**************" );
			LinkedList<EventData> events = new LinkedList<EventData>();
			
			if(count %10 ==0)
			{
				id = id +1;
				id1 = id1 +1;
				fraudcount = fraudcount+1;
		    	System.out.println(" *********** :::::::::::: AFter few second &&&&&&&&&&&&&&&&&&& ::::::::: ");
		    //	LinkedList<EventData> events = new LinkedList<EventData>();
				for ( int i =0; i < 2; i++){
					flag =1;
					if( i ==0){
						System.out.println("not fraud");
		 	PayloadEvent payload = new PayloadEvent(id, rn,flag,fraudcount);
				//	System.out.println("Data sent:" +payload.d1 +", end date:  "+payload.d2);
					System.out.println("Data sent:" +payload.customer_id_from +","+payload.towerID+","+payload.tower_band+" ,"+payload.tower_freq+","+payload.call_quality+","+payload.call_type+","+payload.ratings+" , "+payload.IMSI);
					   	byte[] payloadBytes = gson.toJson(payload).getBytes(Charset.defaultCharset());
					EventData sendEvent = new EventData(payloadBytes);
					Map<String, String> applicationProperties = new HashMap<String, String>();
					applicationProperties.put("from", "javaClient");
					sendEvent.setProperties(applicationProperties);
					events.add(sendEvent); 
					final int[] delay = {1000, 2000,3000,4000,5000};
					Random random = new Random();
					int index = random.nextInt(delay.length);
						
						int del = delay[index]; 
					Thread.sleep(del);
					}// end of if
					else
					{
						System.out.println(" &&&&&&&&&& fraud");
						PayloadEvent payload = new PayloadEvent(id1, rn,flag,fraudcount);
						//	System.out.println("Data sent:" +payload.d1 +", end date:  "+payload.d2);
						System.out.println("Data sent:" +payload.customer_id_from +","+payload.towerID+","+payload.tower_band+" ,"+payload.tower_freq+","+payload.call_quality+","+payload.call_type+","+payload.ratings+" , "+payload.IMSI);
							byte[] payloadBytes = gson.toJson(payload).getBytes(Charset.defaultCharset());
							EventData sendEvent = new EventData(payloadBytes);
							Map<String, String> applicationProperties = new HashMap<String, String>();
							applicationProperties.put("from", "javaClient");
							sendEvent.setProperties(applicationProperties);
							events.add(sendEvent); 
							Thread.sleep(1000);
						
					} //end of else
					
				} // end of for
				
				
				
			}// end of if(count %10 ==0) 
			
			else{
			flag =0;
			for ( int i =0; i < 10; i++){
				
				//random.nextInt(max - min + 1) + min
				int ran_num =rn.nextInt(30 - 1 + 1) + 1;
				//System.out.println("Random number:" +ran_num);
				PayloadEvent payload = new PayloadEvent(ran_num, rn,flag,fraudcount);
			//	System.out.println("Data sent:" +payload.d1 +", end date:  "+payload.d2);
				System.out.println("Data sent:" +payload.customer_id_from +","+payload.towerID+","+payload.tower_band+" ,"+payload.tower_freq+","+payload.call_quality+","+payload.call_type+","+payload.ratings+" , "+payload.IMSI);
				byte[] payloadBytes = gson.toJson(payload).getBytes(Charset.defaultCharset());
				EventData sendEvent = new EventData(payloadBytes);
				Map<String, String> applicationProperties = new HashMap<String, String>();
				applicationProperties.put("from", "javaClient");
				sendEvent.setProperties(applicationProperties);
				events.add(sendEvent); 
				Thread.sleep(100);

			}
				
			}

			sender.send(events).get();
			System.out.println(String.format("Sent Batch... Size: %s ", events.size()));
		//	System.out.println("Also " + sender.send(events).toString());
		}		
	}

	/**
	 * actual application-payload, ex: a CDR event
	 */
	static final class PayloadEvent
	{
		PayloadEvent(final int seed, Random rn, int flag,int fraudcount)
		{
			DataGenerator dg = new DataGenerator();
			this.towerID = "t"+seed;
			String[] tower_details = dg.generate_tower_freq();
			this.tower_freq = Integer.parseInt(tower_details[0]);
			this.tower_band = tower_details[1];
			String[] cust_id =null;
			if(flag ==0 )
			{
			cust_id =dg.generate_from_cust(seed);
			this.customer_id_from = cust_id[0];
			}
			else{
				this.customer_id_from = "c"+seed;
			}
			
			
			this.call_type = dg.generate_call_type(seed);
			if( this.call_type == "Data")
			{
				this.customer_id_to = "N/A";
				
			}
			else
			{
				this.customer_id_to = dg.generate_to_cust(seed);
			}
			
			long t1 = System.currentTimeMillis();
			   long t2 = t1 + 2 * 60 * 1000 + rn.nextInt(60 * 1000) + 1;
			    this.d1 = new DateTime(t1).toString();
			    
			    this.d2 = new DateTime(t2).toString();
			    if ( this.call_type == "sms")
			    {
			    	this.d2 = new DateTime(t1).toString();	
			    }
			    this.call_result = dg.get_call_result(seed, this.call_type);
			    
			   if( this.call_result == "BUSY")
			   {
				   this.d2 = new DateTime(t1).toString();	
			   }
			 //  this.month = new DateTime(t1).getMonthOfYear();
			   this.charge = rn.nextFloat();
			   this.call_quality = dg.get_call_quality(this.call_result,seed, this.call_type);
			   this.call_protocol = dg.get_call_protocol(this.call_type);
			   this.data_speed = dg.get_data_protocol(this.call_type);
			   if( this.call_type == "Data"){
					this.data_usage =rn.nextFloat();
					
				}	
				else
				{
					this.data_usage = (float) 0.0;
					
				} 
			   this.weather = dg.get_weather(this.call_quality);
			   this.ratings=dg.get_ratings(this.call_quality, this.weather);
			   
			   
			//   System.out.println(" CUst no :" + cust_no);
			   
			   if( flag ==0)
			   {
				   int cust_no = Integer.parseInt(cust_id[1]);
				   this.IMSI= dg.get_IMSI( flag,cust_no);
			   }
			   else{
				   this.IMSI= dg.get_IMSI( flag,fraudcount);
			   }
		}
		
		public String towerID;
		public int tower_freq;
		public String tower_band;
		public String customer_id_from;
		public String call_type;
		public String customer_id_to;
	//	public long t1;
	//	public long t2;
		public String  d1;
		public String d2;
	//	public int month;
		public String call_result;
		public Float charge;
		public String call_quality;
		public String call_protocol;
		public String data_speed;
		public Float data_usage; 
		public String weather;
		public String ratings;
		public String IMSI;
		
	}
}