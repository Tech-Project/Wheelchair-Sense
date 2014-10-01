package com.neux.proj.insurance;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.neux.proj.insurance.R;

// ************* WebViewClient Class Begin ***************//
public class MyWebViewClient extends WebViewClient
{
	private Context mContext;
	private AlertDialog alertDialog;
	public MyWebViewClient(Context context)
	{
		mContext = context;
		alertDialog = new AlertDialog.Builder(mContext).create();

	}
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) 
	{

//        final ProgressDialog progressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_TRADITIONAL);
//        // progressDialog.setTitle("°õ¦æ¤¤");
//        progressDialog.setMessage(MainWebViewActivity.getResourceString(R.string.loading));
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.setCancelable(false);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
//
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                progressDialog.dismiss();
//            }
//        };
//
//        Timer timer = new Timer();
//        timer.schedule(task, 1000);

        MainWebViewActivity.showProgressDialog();

		view.loadUrl(url);
        return true;  
	}  
	@Override
	public void onPageFinished(WebView view, String url) 
	{
        MainWebViewActivity.closeProgressDialog();
			//progressBar.dismiss();  
		
		
		// Cloud Added 20131018 Begin .. for FB redirector Bug
		if ((MyUtilityManager.checkStrNotNull(WebViewJSInterface.CompareURL)) && (MyUtilityManager.checkStrNotNull(WebViewJSInterface.RedirectURL)))
		{
		
			String[] compareURLs = WebViewJSInterface.CompareURL.split(";");			
			for(String compareURL:compareURLs)
			{
				if (url.equals(compareURL))
				{
					view.loadUrl(WebViewJSInterface.RedirectURL);
			        return;
				}
					
			}
		}
		
		/*
		Log.e("DEBUG","url = " + url);
		if(url.startsWith("http://www.facebook.com/connect/connect_to_external_page_widget_loggedin.php") ||
				
				 //url.startsWith("https://m.facebook.com/dialog/feed") ||
				("https://m.facebook.com/dialog/feed".equals(url)) ||
				url.startsWith("https://www.facebook.com/plugins/close_popup.php")
		 )
        {
			Log.e("DEBUG", "blanks");
            String redirectUrl =  mContext.getResources().getString(R.string.GetUrl);	// Cloud Added 20130910
    		
            view.loadUrl(redirectUrl);
            return;
        }
        */
        super.onPageFinished(view, url);
        
		// Cloud Added End ..
		
    }  
	@Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) 
	{  
		Toast.makeText(mContext, "Error! " + description, Toast.LENGTH_SHORT).show();  
        alertDialog.setTitle("Error");  
        alertDialog.setMessage(description);  
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() 
        {  
              public void onClick(DialogInterface dialog, int which) 
              {  
	                      return;  
              }  
         });  
         alertDialog.show();  
     }  
       


	private String getRedirectURL(String url,int c) 
	{
		return "http://www.yahoo.com.tw";
		/*
	    return "http://www.facebook.com/plugins/like.php?" + "href="
	            + URLEncoder.encode(mUrl) + "&access_token="
	            + URLEncoder.encode(facebook.getAccessToken());
	            */
	}
}
// ************* WebViewClient Class End ***************//
