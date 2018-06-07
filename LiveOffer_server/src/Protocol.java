import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.*;


public class Protocol {
	
	public double calculateDistance(double lat1, double lon1, double lat2, double lon2)
	{
		double distance = -1;		
		// result is in kilometres		
		
		final int R = 6371; // Radius of the earth
        Double latDistance = Math.toRadians(lat2-lat1);
        Double lonDistance = Math.toRadians(lon2-lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + 
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * 
                   Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        distance = R * c;		
		
		return distance;		
	}
	
	
    public String processInput(double latitude, double longitude, String city,String category) {
        String theOutput = null;
        PreparedStatement statement;
        
        String[] words = category.split("-");
        String categoryQuery = "d.category = '" + words[0] + "'";
        for(int i = 1; i<words.length; i++)
        {
        	categoryQuery = categoryQuery + " OR d.category = '" + words[i] + "'";
        }
        
        
        try {
			statement = Server.connection.prepareStatement("select * from company c,deals d where c.company_id=d.company_id AND d.confirmed = '1' AND c.city = '" + city + "' AND (" + categoryQuery + ")");
			
			ResultSet  results = statement.executeQuery();
			System.out.println("Received results from the database.");
			
			//JsonArray array = Json.createArrayBuilder().build();
			JSONArray jArray = new JSONArray();
			
			while(results.next())
			{
				String title, description, companyName, companyAddress;
				double lat, lon, distance;
				
				lat = results.getDouble("latitude");
				lon = results.getDouble("longitude");
				distance = calculateDistance(latitude, longitude, lat, lon);
				String latString = ""+lat;
				String lonString = ""+lon;

				
				if(distance <= 5.0)
				{
					title = results.getString("title");
					description = results.getString("description");
					companyName = results.getString("company_name");
					companyAddress = results.getString("address");
					
					
					
					System.out.println("Distance is: " + distance);
					
					System.out.println("Title: " + title);
					System.out.println("Description: " + description);
					System.out.println("Company Name: " + companyName);
					System.out.println("Company Address: " + companyAddress);
					
					JSONObject json = new JSONObject();
					try {
						json.put("title", title);
						json.put("description", description);
						json.put("company", companyName);
						json.put("address", companyAddress);
						json.put("latitude", latString);
						json.put("longitude", lonString);
						
						
						jArray.put(json);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				
				
				
				/*double lt = results.getDouble(1);
				double ln = results.getDouble(2);
				String cty = results.getString(3);
				System.out.println(lt + " " + ln + " " + cty);*/
			}
			
			String jsonResult = jArray.toString();
			System.out.println("JSON Results: " + jsonResult);
			theOutput = jsonResult;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
        
     
        return theOutput;
    }
}