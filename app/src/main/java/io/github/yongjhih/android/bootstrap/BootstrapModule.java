package io.github.yongjhih.android.bootstrap;

import android.accounts.AccountManager;
import android.content.Context;

import io.github.yongjhih.android.bootstrap.authenticator.ApiKeyProvider;
import io.github.yongjhih.android.bootstrap.authenticator.BootstrapAuthenticatorActivity;
import io.github.yongjhih.android.bootstrap.authenticator.LogoutService;
import io.github.yongjhih.android.bootstrap.core.BootstrapService;
import io.github.yongjhih.android.bootstrap.core.Constants;
import io.github.yongjhih.android.bootstrap.core.PostFromAnyThreadBus;
import io.github.yongjhih.android.bootstrap.core.RestAdapterRequestInterceptor;
import io.github.yongjhih.android.bootstrap.core.RestErrorHandler;
import io.github.yongjhih.android.bootstrap.core.TimerService;
import io.github.yongjhih.android.bootstrap.core.UserAgentProvider;
import io.github.yongjhih.android.bootstrap.ui.BootstrapTimerActivity;
import io.github.yongjhih.android.bootstrap.ui.CheckInsListFragment;
import io.github.yongjhih.android.bootstrap.ui.MainActivity;
import io.github.yongjhih.android.bootstrap.ui.NavigationDrawerFragment;
import io.github.yongjhih.android.bootstrap.ui.NewsActivity;
import io.github.yongjhih.android.bootstrap.ui.NewsListFragment;
import io.github.yongjhih.android.bootstrap.ui.UserActivity;
import io.github.yongjhih.android.bootstrap.ui.UserListFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Dagger module for setting up provides statements.
 * Register all of your entry points below.
 */
@Module(
        complete = false,

        injects = {
                BootstrapApplication.class,
                BootstrapAuthenticatorActivity.class,
                MainActivity.class,
                BootstrapTimerActivity.class,
                CheckInsListFragment.class,
                NavigationDrawerFragment.class,
                NewsActivity.class,
                NewsListFragment.class,
                UserActivity.class,
                UserListFragment.class,
                TimerService.class
        }
)
public class BootstrapModule {

    @Singleton
    @Provides
    Bus provideOttoBus() {
        return new PostFromAnyThreadBus();
    }

    @Provides
    @Singleton
    LogoutService provideLogoutService(final Context context, final AccountManager accountManager) {
        return new LogoutService(context, accountManager);
    }

    @Provides
    BootstrapService provideBootstrapService(RestAdapter restAdapter) {
        return new BootstrapService(restAdapter);
    }

    @Provides
    BootstrapServiceProvider provideBootstrapServiceProvider(RestAdapter restAdapter, ApiKeyProvider apiKeyProvider) {
        return new BootstrapServiceProvider(restAdapter, apiKeyProvider);
    }

    @Provides
    ApiKeyProvider provideApiKeyProvider(AccountManager accountManager) {
        return new ApiKeyProvider(accountManager);
    }

    @Provides
    Gson provideGson() {
        /**
         * GSON instance to use for all request  with date format set up for proper parsing.
         * <p/>
         * You can also configure GSON with different naming policies for your API.
         * Maybe your API is Rails API and all json values are lower case with an underscore,
         * like this "first_name" instead of "firstName".
         * You can configure GSON as such below.
         * <p/>
         *
         * public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd")
         *         .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create();
         */
        return new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }

    @Provides
    RestErrorHandler provideRestErrorHandler(Bus bus) {
        return new RestErrorHandler(bus);
    }

    @Provides
    RestAdapterRequestInterceptor provideRestAdapterRequestInterceptor(UserAgentProvider userAgentProvider) {
        return new RestAdapterRequestInterceptor(userAgentProvider);
    }

    @Provides
    RestAdapter provideRestAdapter(RestErrorHandler restErrorHandler, RestAdapterRequestInterceptor restRequestInterceptor, Gson gson) {
        return new RestAdapter.Builder()
                .setEndpoint(Constants.Http.URL_BASE)
                .setErrorHandler(restErrorHandler)
                .setRequestInterceptor(restRequestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();
    }

}
