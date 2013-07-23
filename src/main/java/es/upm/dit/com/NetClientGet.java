package es.upm.dit.com;
import java.io.BufferedReader;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.Iterator;
import java.io.InputStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

 
public class NetClientGet {
 
	// http://localhost:8080/RESTfulExample/json/product/get
	public static void main(String[] args) throws Exception {
 

 
		URL url = new URL("http://glacial-refuge-5887.herokuapp.com/json");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
 
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}
 
		BufferedReader br = new BufferedReader(new InputStreamReader(
			(conn.getInputStream())));
		String textoJSON="";
		String output;
		System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			textoJSON+=output;
			System.out.println(output);
		}
 
		conn.disconnect();
 
	  DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	  DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
	  JSONObject json = (JSONObject) JSONSerializer.toJSON(textoJSON);
	  JSONArray medidas = json.getJSONArray("medidas");
	  //Scanner para pedir las
	  Scanner nuevoScanner=new Scanner(System.in);
	  System.out.println("¿Que gŕafica quieres?");
	  System.out.println("1.-Freememory");
	  System.out.println("2.-CPUtimes");
	  int tipoGrafica=nuevoScanner.nextInt();
	  /**/
	   
	  JFreeChart chart=null;
	  ChartFrame frame=null;
      switch (tipoGrafica){
      	case 1:
      		for (int i=0;i<medidas.size();i++){
      		  dataset.addValue(medidas.getJSONObject(i).getLong("freememory"),"Medida",medidas.getJSONObject(i).getString("date"));
      	  }
      		chart = ChartFactory.createBarChart("Free Memory",
    				"Fecha",
    				"Bytes",
    				dataset,	//Dataset
    				PlotOrientation.VERTICAL,
    				true,
    				true,
    				false);
    		//crear y visualizar una ventana...
    		frame = new ChartFrame("Grafica Free Memory", chart);
      		break;
      	case 2:
      		JSONObject cputimes=null;
      	  
      	  for(int i=0;i<medidas.size();i++){
      		  cputimes =medidas.getJSONObject(i).getJSONObject("cputimes");
      		  
      		  for(int j=0;j<4;j+=1){
      			Iterator iterator=cputimes.keys();
      			  while(iterator.hasNext()){
      				  Object clave =iterator.next();
      				  dataset2.addValue(((JSONArray)cputimes.get(clave)).getLong(j), clave.toString(), "Core:"+(j+1)+"-"+medidas.getJSONObject(i).getString("date"));
      			  }
      		  }
      	  }
      	chart = ChartFactory.createBarChart("CPU Times",
				"Fecha",
				"Tiempo",
				dataset2,	//Dataset
				PlotOrientation.VERTICAL,
				true,
				true,
				false);
		//crear y visualizar una ventana...
		frame = new ChartFrame("Grafica CPU Times", chart);
      		break;
      }	 
	  
	//Crear el dataset...
		
		
		//Crear el gráfico...
		
		frame.pack();
		frame.setVisible(true);
	  
	}	
	  
	

	  
}