package cy.com.partnermall;

import android.app.Application;
import android.content.res.Configuration;
import cy.com.partnermall.config.Constants;
import cy.com.partnermall.image.ImageLoaderConfig;

public class BaseApplication extends Application {

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		ImageLoaderConfig.initImageLoader(this, Constants.BASE_IMAGE_CACHE);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

}
