package com.kokoa.huecapp.fragments;

import com.kokoa.huecapp.R;
import com.kokoa.huecapp.function.NetworkFunction;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class TopFragment extends Fragment {

	public WebView webView;
	public View rootView;
	//public ProgressDialog pd;
	public String url="http://104.131.50.216:8888/top/";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_top, container, false);
		webView = (WebView) rootView.findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		
		if (!NetworkFunction.verificaConexion(getActivity())) {
			Toast.makeText(getActivity(),
					"No tiene Coneccion a Internet", Toast.LENGTH_LONG).show();
		} else {
			
			try{
				
				loadMap();
    				
    		}catch(Exception e){
    			Toast.makeText(getActivity(), "Problemas con el GPS Network", Toast.LENGTH_LONG).show();
    			
    		}
			
		}
		
		return rootView;
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	public void loadMap(){
		//webView.getSettings().setJavaScriptEnabled(true);

		DialogInterface.OnCancelListener dialogCancel = new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				Toast.makeText(getActivity(),
						"No hay datos en la fuente de informacion.",
						Toast.LENGTH_LONG).show();
			}
		};
		
		//pd = ProgressDialog.show(getActivity(), "Cargando Ubicacion ...",
		//		"Gracias por su espera", true, true, dialogCancel);
				

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				//pd.dismiss();
				
			}
		});

		webView.loadUrl(url);
	}
	
}
