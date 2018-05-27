package com.udacity.gradle.builditbigger;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Button;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    String mJoke;
    private CountDownLatch mSignal;

    @Before
    public void setUp() {
        mSignal = new CountDownLatch(1);
    }

    @After
    public void tearDown() {
        mSignal.countDown();
    }

    @Test
    public void shouldFetchData() throws InterruptedException {
        new EndpointAsyncTask(new EndpointAsyncTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String j) {
                mJoke = j;
                mSignal.countDown();
            }
        }).execute();
        mSignal.await();

        assertThat(mJoke, not(isEmptyString()));
    }

    @Test
    public void shouldDisplayJoke() {
        onView(isAssignableFrom(Button.class))
                .perform(click());
        onView(isAssignableFrom(TextView.class))
                .check(matches(withText(not(isEmptyString()))));
    }


}