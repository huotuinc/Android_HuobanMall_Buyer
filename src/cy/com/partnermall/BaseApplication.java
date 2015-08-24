package cy.com.partnermall;

import android.app.Application;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;

import cy.com.partnermall.config.Constants;
import cy.com.partnermall.image.ImageLoaderConfig;

public class BaseApplication extends Application {

	public DrawerLayout layDrag;

	@Override
	public
	void onConfigurationChanged ( Configuration newConfig ) {
		super.onConfigurationChanged ( newConfig );
	}

	@Override
	public
	void onCreate ( ) {
		super.onCreate ( );
		ImageLoaderConfig.initImageLoader ( this, Constants.BASE_IMAGE_CACHE );
	}

	@Override
	public
	void onLowMemory ( ) {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

}
