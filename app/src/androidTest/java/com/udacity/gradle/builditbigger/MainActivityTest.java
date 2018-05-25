package com.udacity.gradle.builditbigger;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Button;
import android.widget.TextView;

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

    @Test
    public void shouldFetchData() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        new EndpointAsyncTask(new EndpointAsyncTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String joke) {
                assertThat(joke, not(isEmptyString()));
                signal.countDown();
            }
        }).execute();
        signal.await();
    }

    @Test
    public void shouldDisplayJoke() {
        onView(isAssignableFrom(Button.class))
                .perform(click());
        onView(isAssignableFrom(TextView.class))
                .check(matches(withText(not(isEmptyString()))));
    }


}