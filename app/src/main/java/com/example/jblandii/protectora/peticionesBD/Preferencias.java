package com.example.jblandii.protectora.peticionesBD;


import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


public class Preferencias{
	
	public static String getID(Activity actividad){
		String token = getToken(actividad);
		if(token != null && !token.equals(""))
			return token.substring(0, token.indexOf("_"));
		else
			return -1+"";
	}


	public static void guardarEnPref(Activity actividad, String usuario, String token){
		Log.v("preferencias", "entor");
		//SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences pref = actividad.getSharedPreferences("preferencias",actividad.MODE_PRIVATE);
		Log.v("preferencias", ""+pref);
		Editor edit=(pref.edit());
		edit.putString("usuario", usuario);
		edit.putString("token",token);
		//edit.apply();
		edit.commit();
	}
		
	public static String getToken(Activity actividad){
		SharedPreferences pref = actividad.getSharedPreferences("preferencias",actividad.MODE_PRIVATE);
		Log.v("logout", "preferencias "+pref);
		String token=pref.getString("token",null);
		Log.v("logout", "preferencias "+token);
		return token;
	}
}
