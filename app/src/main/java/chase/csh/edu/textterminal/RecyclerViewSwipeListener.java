package chase.csh.edu.textterminal;

import android.animation.Animator;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;

/**
 * Created by chase on 1/24/15.
 */
public class RecyclerViewSwipeListener {
    //TODO cleanup this so it functions much more smoothly
    public static void bindListenerToView(final View view, final RecyclerView.ViewHolder holder, final Callback callback) {

        final GestureDetectorCompat gestureDetector = new GestureDetectorCompat(view.getContext(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        final int viewSwipeThreshold = view.getWidth() / 4;
                        if (velocityX < -viewSwipeThreshold) {
                            onSwipe(view, holder.getPosition(), false, callback);
                            return true;
                        } else if (velocityX > viewSwipeThreshold) {
                            onSwipe(view, holder.getPosition(), true, callback);
                            return true;
                        }
                        return false;
                    }
                });

        View.OnTouchListener viewTouchListener = new View.OnTouchListener() {
            private final float origin = 0;

            private float startMoveX = 0;
            private float startMoveY = 0;

            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                final int viewSwipeHorizontalThreshold = view.getWidth() / 3;
                /* final int viewSwipeVerticalThreshold = view.getContext().getResources()
                        .getDimensionPixelSize(R.dimen.vertical_swipe_threshold); */
                if (gestureDetector.onTouchEvent(event))
                    return true;

                final float x = event.getRawX(), y = event.getRawY();
                final float deltaX = x - startMoveX, deltaY = y - startMoveY;

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        startMoveX = x;
                        startMoveY = y;
                        break;

                    case MotionEvent.ACTION_UP:
                        if (Math.abs(deltaX) < viewSwipeHorizontalThreshold) {

                            view.animate().translationX(origin).translationY(origin).alpha(1).start();

                            if (Math.abs(deltaY) < viewSwipeHorizontalThreshold) {//TODO Y, HORZ threshold..?
                                view.performClick();//TODO is needed?
                            }


                        } else {
                            onSwipe(view, holder.getPosition(), deltaX < 0, callback);
                        }
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        if (Math.abs(deltaX) < viewSwipeHorizontalThreshold) {
                            view.animate().translationX(origin).translationY(origin).alpha(1).start();
                        } else {
                            onSwipe(view, holder.getPosition(), deltaX < 0, callback);
                        }
                        break;

                    case MotionEvent.ACTION_POINTER_DOWN:
                    case MotionEvent.ACTION_POINTER_UP:
                        break;

                    case MotionEvent.ACTION_MOVE:
                        view.setAlpha(Math.max(Math.min((255 - Math.abs(deltaX)) / 255f, 1.0f), 0.1f));
                        view.setTranslationX(deltaX);
                        break;
                }
                return true;
            }
        };
        view.setOnTouchListener(viewTouchListener);
    }


    private static void onSwipe(final View view, final int position, final boolean isToLeft, final Callback callback) {
        ViewPropertyAnimator animator = view.animate().translationX(view.getWidth() * (isToLeft ? -1 : 1));
        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setAlpha(1);
                callback.viewSwiped(position);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.start();
    }


    public static abstract class Callback {

        public abstract void viewSwiped(int viewPosition);

    }

}
