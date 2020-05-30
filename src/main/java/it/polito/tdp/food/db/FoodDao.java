package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Coppie;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public List<Food> listAllFoods(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public void getCibiPerPorzione(Map<Integer, Food> mappaCibi, Integer porzioni){
		String sql = "SELECT food.food_code, food.display_name, t1.cnt FROM (SELECT food_code, COUNT(*) AS cnt FROM `portion` GROUP BY food_code) AS t1, food WHERE food.food_code=t1.food_code AND t1.cnt=? ORDER BY food.display_name" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, porzioni);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Food cibo = new Food(res.getInt(1), res.getString(2));
				if(!mappaCibi.containsKey(cibo.getFood_code()))
					mappaCibi.put(cibo.getFood_code(), cibo);
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Coppie> listAllCoppie(Map<Integer, Food> mappaCibi){
		String sql = "SELECT f1.food_code, f2.food_code, AVG(c1.condiment_calories)" + 
				" FROM condiment AS c1, food_condiment AS f1, food_condiment AS f2" + 
				" WHERE c1.condiment_code=f1.condiment_code AND c1.condiment_code=f2.condiment_code AND f1.food_code<f2.food_code" + 
				" AND f1.condiment_code=f2.condiment_code" + 
				" GROUP BY f1.food_code, f2.food_code" ;
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			List<Coppie> list = new ArrayList<>() ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Food f1 = mappaCibi.get(res.getInt(1));
				Food f2 = mappaCibi.get(res.getInt(2));
				if(f1!=null && f2!= null)
					list.add(new Coppie(f1, f2, res.getDouble(3)));
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	
	
	
}
