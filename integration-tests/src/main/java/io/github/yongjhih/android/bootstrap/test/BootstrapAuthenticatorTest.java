

package io.github.yongjhih.android.bootstrap.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;

import io.github.yongjhih.android.bootstrap.authenticator.BootstrapAuthenticatorActivity;
import io.github.yongjhih.android.bootstrap.R;


/**
 * Tests of displaying the authenticator activity
 */
public class BootstrapAuthenticatorTest extends ActivityInstrumentationTestCase2<BootstrapAuthenticatorActivity> {

    /**
     * Create test for {@link io.github.yongjhih.android.bootstrap.authenticator.BootstrapAuthenticatorActivity}
     */
    public BootstrapAuthenticatorTest() {
        super(BootstrapAuthenticatorActivity.class);
    }

    /**
     * Verify activity exists
     */
    public void testActivityExists() {
        assertNotNull(getActivity());
    }

    /**
     * Verify sign in button is initially disabled
     */
    public void testSignInDisabled() {
        View view = getActivity().findViewById(R.id.b_signin);
        assertNotNull(view);
        assertTrue(view instanceof Button);
        assertFalse(((Button) view).isEnabled());
    }
}
