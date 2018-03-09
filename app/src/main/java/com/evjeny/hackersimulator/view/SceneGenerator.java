package com.evjeny.hackersimulator.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evjeny.hackersimulator.R;
import com.evjeny.hackersimulator.game.Act;
import com.evjeny.hackersimulator.game.Bar;
import com.evjeny.hackersimulator.game.Button;
import com.evjeny.hackersimulator.game.CodeContent;
import com.evjeny.hackersimulator.game.CodePart;
import com.evjeny.hackersimulator.game.GameType;
import com.evjeny.hackersimulator.game.ImageContent;
import com.evjeny.hackersimulator.game.Scene;
import com.evjeny.hackersimulator.game.StoryContent;
import com.pixplicity.htmlcompat.HtmlCompat;

import java.util.ArrayList;

/**
 * Created by evjeny on 21.01.2018 10:08 6:41.
 */

class SceneGenerator {
    private Context context;
    private FragmentManager manager;
    private ViewGroup fragmentHolder, buttonHolder;

    private Bundle args;

    private actPointerInterface actPointerInterface = null;
    private int actPointer = 0;

    SceneGenerator(Context context, FragmentManager manager, ViewGroup fragmentHolder,
                   ViewGroup buttonHolder, GameType type) {
        this.context = context;
        this.manager = manager;
        this.fragmentHolder = fragmentHolder;
        this.buttonHolder = buttonHolder;
        this.args = new Bundle();
        int style = type == GameType.proger ? R.style.ProgerTheme : R.style.HackerTheme;
        this.args.putInt("style", style);
    }

    void generateScene(final Scene scene, final storyInterface intf) {
        final ArrayList<Act> acts = scene.acts;
        actPointer = 0;
        actPointerInterface = new actPointerInterface() {
            @Override
            public void nextAct() {
                if (actPointer < acts.size()) {
                    Act current = acts.get(actPointer);
                    generateActAuto(current, intf);
                } else {
                    intf.sceneEnd();
                }
            }
        };
        actPointerInterface.nextAct();
    }

    private void generateActAuto(final Act act, final storyInterface intf) {
        intf.actStart();
        switch (act.type) {
            case STORY:
                generateStoryAct(act, intf);
                break;
            case IMAGE:
                generateImageAct(act, intf);
                break;
            case CODE:
                generateCodeAct(act, intf);
                break;
            default:
                generateStoryAct(act, intf);
                break;
        }
    }

    private void generateStoryAct(final Act act, final storyInterface intf) {
        final MessageFragment messageFragment = new MessageFragment();
        messageFragment.setArguments(args);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(fragmentHolder.getId(), messageFragment);
        transaction.runOnCommit(new Runnable() {
            @Override
            public void run() {
                final TextView mf_tv = messageFragment.getView().findViewById(R.id.message_fragment_tv);
                final StoryContent content = (StoryContent) act.content;
                final int[] pointer = {0};
                mf_tv.setText(HtmlCompat.fromHtml(context, content.getMessage(pointer[0]),
                        HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS));
                Bar bar = act.bar;
                buttonHolder.removeAllViews();
                for (final Button button : bar.buttons) {
                    android.widget.Button b = new android.widget.Button(context);
                    LinearLayout.LayoutParams params =
                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    b.setLayoutParams(params);

                    b.setText((String) button.name);

                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (button.action.equals("next")) {
                                pointer[0]++;
                                if (pointer[0] < content.getMessages().size()) {
                                    intf.nextContent();

                                    mf_tv.setText(HtmlCompat.fromHtml(context, content.getMessage(pointer[0]),
                                            HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS));
                                } else {
                                    intf.actEnd();
                                    sendNextAct();
                                }
                            } else if (button.action.equals("stop")) {
                                intf.stop();
                            }
                        }
                    });
                    buttonHolder.addView(b);
                }
            }
        });
        transaction.commit();
    }

    private void generateImageAct(final Act act, final storyInterface intf) {
        final ImageFragment imageFragment = new ImageFragment();
        imageFragment.setArguments(args);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(fragmentHolder.getId(), imageFragment);
        transaction.runOnCommit(new Runnable() {
            @Override
            public void run() {
                final TImageView imageView =
                        imageFragment.getView().findViewById(R.id.image_fragment_imageview);
                final ImageContent content = (ImageContent) act.content;
                final int[] pointer = {0};
                imageView.generateImage(content.getImage(pointer[0]));
                Bar bar = act.bar;
                buttonHolder.removeAllViews();
                for (final Button button : bar.buttons) {
                    android.widget.Button b = new android.widget.Button(context);
                    LinearLayout.LayoutParams params =
                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    b.setLayoutParams(params);

                    b.setText((String) button.name);

                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (button.action.equals("next")) {
                                pointer[0]++;
                                if (pointer[0] < content.getImages().size()) {
                                    intf.nextContent();

                                    imageView.generateImage(content.getImage(pointer[0]));
                                } else {
                                    intf.actEnd();
                                    sendNextAct();
                                }
                            } else if (button.action.equals("stop")) {
                                intf.stop();
                            }
                        }
                    });
                    buttonHolder.addView(b);
                }
            }
        });
        transaction.commit();
    }

    private void generateCodeAct(final Act act, final storyInterface intf) {
        final CodeFragment codeFragment = new CodeFragment();
        codeFragment.setArguments(args);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(fragmentHolder.getId(), codeFragment);
        transaction.runOnCommit(new Runnable() {
            @Override
            public void run() {
                final LinearLayout holder =
                        codeFragment.getView().findViewById(R.id.code_fragment_holder);
                final CodeContent content = (CodeContent) act.content;
                final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                final ArrayList<TextView> parts = new ArrayList<>();

                for (CodePart codePart : content.getFragments()) {
                    if (codePart.type == CodePart.Type.TEXT) {
                        TextView textView = new TextView(context);
                        textView.setText(codePart.value);
                        textView.setLayoutParams(params);
                        holder.addView(textView);
                        parts.add(textView);
                    } else if (codePart.type == CodePart.Type.WRITABLE) {
                        EditText editText = new EditText(context);
                        editText.setHint(codePart.value);
                        editText.setLayoutParams(params);
                        holder.addView(editText);
                        parts.add(editText);
                    }
                }
                Bar bar = act.bar;
                buttonHolder.removeAllViews();
                for (final Button button : bar.buttons) {
                    android.widget.Button b = new android.widget.Button(context);

                    b.setLayoutParams(params);

                    b.setText((String) button.name);

                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (button.action.equals("check")) {
                                ArrayList<String> res = new ArrayList<>();
                                for (TextView v : parts) res.add(v.getText().toString());
                                intf.check(res);
                                intf.actEnd();
                                sendNextAct();
                            } else if (button.action.equals("stop")) {
                                intf.stop();
                            }
                        }
                    });
                    buttonHolder.addView(b);
                }
            }
        });
        transaction.commit();
    }

    private void sendNextAct() {
        actPointer++;
        actPointerInterface.nextAct();
    }

    public interface storyInterface {

        void actStart();

        void nextContent();

        void actEnd();

        void stop();

        void check(ArrayList<String> code);

        void sceneEnd();
    }

    private interface actPointerInterface {
        void nextAct();
    }
}
