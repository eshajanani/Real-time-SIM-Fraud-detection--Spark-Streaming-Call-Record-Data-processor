package azureSender;

import java.util.Random;

import org.joda.time.DateTime;


public class DataGenerator {
	
	
	// function to generate from customer id	
	public  String[] generate_from_cust(int num)
	{
		Random rn = new Random();
		//random.nextInt(max - min + 1) + min
		String cust_id[] =new String[2];
		
		if( num <=10)
		{
			int ran_num = rn.nextInt(10 - 1 + 1) + 1;
			cust_id[0] = "c"+ran_num;
			cust_id[1]=Integer.toString(ran_num);
		}

		else if( num > 10 && num <=20 )
		{
			int ran_num = rn.nextInt(20 - 11 + 1) + 11;
			cust_id[0] = "c"+ran_num;
			cust_id[1]=Integer.toString(ran_num);
		}
		else if( num > 20 && num <=30 )
		{
			int ran_num = rn.nextInt( 30 - 21 + 1) + 21;
			cust_id[0] = "c"+ran_num;
			cust_id[1]=Integer.toString(ran_num);
		}
		else{
			int ran_num = rn.nextInt(90 -31 + 1) + 31;
			cust_id[0] = "c"+ran_num;
			cust_id[1]=Integer.toString(ran_num);
		}
		return cust_id;
	}
	// end of generate_from_cust
	
	// function to generate from generate_call_type
	
		public  String generate_call_type(int ran_num)
		{
			
			String call_type;
			
			if( ran_num % 5 == 0 & ran_num <=30 )
			{
				call_type= "sms";
			}
			else if(ran_num %3==0 && ran_num <=30)
			{
				call_type= "Data";
			}
			else if ( ran_num % 2 == 0)
			{
				call_type = "Call";
			}
			else
			{
				call_type ="Call";
			}
			
			/* final String[] proper_noun = {"Fred", "Jane", "Richard Nixon", "Miss America"};
			Random random = new Random();
			int index = random.nextInt(proper_noun.length);
			call_type=proper_noun[index]; */
			
			return call_type;
		
		}// end of generate_call_type
		
		
		// function to generate to customer id
		
		public  String generate_to_cust(int num)
		{
			Random rn = new Random();
			//random.nextInt(max - min + 1) + min
			String cust_id;
			
			if( num <= 10)
			{
				int ran_num = rn.nextInt(110 - 101 + 1) + 101;
				cust_id = "c"+ran_num;
			}

			else if( num > 10 && num <=20 )
			{
				int ran_num = rn.nextInt(120 - 111 + 1) + 111;
				cust_id = "c"+ran_num;
			}
			
			else if( num > 20 && num <=30 ){
				int ran_num = rn.nextInt(130 - 121 + 1) + 121;
				cust_id = "c"+ran_num;
			}
			
			else {
				int ran_num = rn.nextInt(140 - 131 + 1) + 131;
				cust_id = "c"+ran_num;
			
			}
			return cust_id;
		}// end of generate_to_cust
		
		
		// function to generate call_result
		
		public  String get_call_result(int ran_num, String call_type)
		{
			String call_result;
			
			 if ( ran_num  % 7 == 0 && call_type == "Call") {
			    	call_result = "BUSY";
					
				}
			    else if(  ran_num  % 7 != 0 && call_type == "Call")
			    {
			    	call_result ="Answered";
			    
			    }
			    else
			    {
			    	call_result = "N/A";
			    }
			
			return call_result;
		}
		// end of get_call_result
		
		
		// function to generate call_quality
		
		public  String get_call_quality( String call_result, int ran_num, String call_type)
		{
			String call_quality;
			
			if( call_result == "Answered" && call_type =="Call"&& ran_num %2 != 0)
			{
				call_quality = "Dropped";
			}
			else if ( call_result == "Answered" && call_type =="Call" && ran_num %2 ==0)
			{
				call_quality ="Good";
			}
			else
			{
				call_quality = "N/A";
			}
			return call_quality;
			
		} // end of call_quality
		
		
		//function to generate call_protocol
		
		public  String get_call_protocol(String call_type)
		{
			
			String call_protocol;
			
			final String[] protocols = {"MGCP", "SIP", "H232", "SGCP"};
			Random random = new Random();
			int index = random.nextInt(protocols.length);
			
			
			if( call_type == "Call"){
				
				call_protocol = protocols[index]; 
			}
			else
			{
				call_protocol = "N/A";
			}
			
			return call_protocol;
			
		} // end of get_call_protocol
		
		//function to generate weather
		
		public  String get_ratings(String call_quality, String weather)
		{
			
			String ratings;
			
			final String[] ratings_list = { "Sunny", "Cloudy","Foggy"};
			
			final String[] weather_list2 = {"Rainy", "Thuderstorm","Windy"};
			
			
			
			if( call_quality == "Dropped"){
				if(weather == "Rainy" || weather =="Thuderstorm")
				{
					ratings ="Very Bad";
				}
				else{
					
					ratings ="Bad";
				}
			}
			else
			{
				ratings ="Good";
			}
			
			return ratings;
			
		} //end of get_ratings	
		//function to generate weather
		
		public  String get_weather(String call_quality)
		{
			
			String weather;
			
			final String[] weather_list1 = { "Sunny", "Cloudy","Foggy"};
			
			final String[] weather_list2 = {"Rainy", "Thuderstorm","Windy"};
			
			
			
			if( call_quality == "Dropped"){
				Random random = new Random();
			int index = random.nextInt(weather_list2.length);
				
				weather = weather_list2[index]; 
			}
			else
			{
				Random random = new Random();
				int index = random.nextInt(weather_list1.length);
				weather = weather_list1[index]; 
			}
			
			return weather;
			
		} //end of get_data_protocol	
		
		
		//function to generate data_protocol
		
			public  String get_data_protocol(String call_type)
			{
				
				String data_speed;
				
				final String[] speed = {"3G", "4G", "LTE"};
				Random random = new Random();
				int index = random.nextInt(speed.length);
				
				
				if( call_type == "Data"){
					
					data_speed = speed[index]; 
				}
				else
				{
					data_speed = "N/A";
				}
				
				return data_speed;
				
			} //end of get_data_protocol	
			
			
			//function to generate data_protocol
			
			public  String[] generate_tower_freq()
			{
				
				String[] tower_details = new String[2];
				
				final String[] t_speed = {"1900", "1700", "700"};
				Random random = new Random();
				int index = random.nextInt(t_speed.length);
				
				
				tower_details[0] = t_speed[index];
				
				if( tower_details[0] == "1900")
				{
					tower_details[1] = "Band 2";
				}
				else if( tower_details[0] == "1700")
				{
					tower_details[1] = "Band 4";
				}
				else
				{
					tower_details[1] = "Band 12";
				}
				
				
				return tower_details;
				
			} //end of get_data_protocol	
			
			
			public String get_IMSI(int flag,int index)
			{
				String IMSI;
				
				 String[] MSRNList = { "886932428687", "886932429021", "886932428306", "1415982715962", "886932429979", "1416916990491", "886937415371", "886932428876", "886932428688", "1412983121877", "886932429242", "1416955584542", "886932428258", "1412930064972", "886932429155", "886932423548", "1415980332015", "14290800303585", "14290800033338", "886932429626", "886932428112", "1417955696232", "1418986850453", "886932428927", "886932429827", "886932429507", "1416960750071", "886932428242", "886932428134", "886932429825" ,"886932429889","886932429845"};
				 String[] MSRNList1 = { "986932428687", "986932429021", "986932428306", "915982715962", "986932429979", "9416916990491", "986937415371","986932428876", "986932428688", "912983121877", "986932429242", "916955584542", "986932428258", "912930064972", "986932429155", "986932423548", "915980332015", "9290800303585", "9290800033338", "986932429626", "986932428112", "917955696232", "918986850453", "986932428927", "986932429827", "986932429507", "916960750071", "986932428242", "986932428134", "986932429825" ,"986932429889","986932429845"};
				 
				 if( flag ==0){
				 IMSI = MSRNList[index];
				 }
				 
				 else
				 {
					 IMSI = MSRNList1[index];
					 
				 }
				return IMSI;
			}
		
		

}
