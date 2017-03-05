package edu.lehigh.cse216_luna.phase1.android.ajv218.phase1_android;


import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.AllOf.allOf;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import android.support.test.espresso.contrib.RecyclerViewActions;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        // ESPRESSO TESTS FOR PHASE 1:
        // TEST 1: Check that some text "A" (in the first TextView) displays in the app.
        ViewInteraction t1 = onView(withText("A"));
        t1.check(matches(isDisplayed()));

        // TEST 2.1: Check that the add button displays AlertDialog box (then cancel add).
        onView(withId(R.id.btn_add)).perform(click());                      // First, click the button.
        onView(withText("Input New Data")).check(matches(isDisplayed()));   // Then, check if window appears.
        onView(withId(android.R.id.button2)).perform(click());              // Finally, click cancel.

        // TEST 2.2: Check that the add button works and add an item to it. Check it has been added.
        onView(withId(R.id.btn_add)).perform(click());                      // First, click the button.
        onView(withText("Input New Data")).check(matches(isDisplayed()));   // Then, check if window appears.
        onView(withHint("Enter data here!")).perform(typeText("TESTDATA")); // Enter some new data.
        onView(withId(android.R.id.button1)).perform(click());              // Click OK.
        onView(withText("TESTDATA")).check(matches(isDisplayed()));         // Finally, check that it's been added.

        // TEST 3: Check that liking a message triggers the Toast message to appear.
        onView(withId(R.id.recycler_view))                                  // Choose RecyclerView...
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,      // Choose its first item...
                        MyViewAction.clickChildViewWithId(R.id.ib_like)));  // Click that item's like button.
        //onView(withText("Liked : A")).check(matches(isDisplayed()));      // Finally, check that this string appears.

        // NOTE: Since the "like" button currently has no functionality, I've limited TEST 3 to just clicking the
        // like button. There is no thread-safe way to test Toast messages in Espresso (where "toast" was the only
        // current action for "liking" something) so I will be putting this part off until "like" actually has some
        // visible impact, hopefully in Phase 2.

        // TEST 4: Check that deleting a message works properly.
        onView(withId(R.id.recycler_view))                                  // Choose RecyclerView...
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,      // Choose its first item...
                        MyViewAction.clickChildViewWithId(R.id.ib_remove)));// Click that item's delete button.
        onView(isRoot()).perform(waitFor(100));                             // Wait (to clear screen)-- min 0.1 secs.
        onView(withText("TESTDATA")).check(doesNotExist());                 // Check that item has been removed.
        //onView(isRoot()).perform(waitFor(5000));                          // As long as Toast messages come up.
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    /**
     * Perform action of waiting for a specific time.
     */
    public static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }
}