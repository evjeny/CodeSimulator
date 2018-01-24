package com.evjeny.hackersimulator.view;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evjeny.hackersimulator.R;
import com.evjeny.hackersimulator.game.Bar;
import com.evjeny.hackersimulator.game.Button;
import com.evjeny.hackersimulator.game.CodeContent;
import com.evjeny.hackersimulator.game.CodePart;
import com.evjeny.hackersimulator.game.ImageContent;
import com.evjeny.hackersimulator.game.Scene;
import com.evjeny.hackersimulator.game.StoryContent;

import java.util.ArrayList;

/**
 * Created by Evjeny on 21.01.2018 10:08.
 */

public class SceneGenerator {
    private Context context;
    private FragmentManager manager;
    private ViewGroup fragmentHolder, buttonHolder;

    public SceneGenerator(Context context, FragmentManager manager, ViewGroup fragmentHolder, ViewGroup buttonHolder) {
        this.context = context;
        this.manager = manager;
        this.fragmentHolder = fragmentHolder;
        this.buttonHolder = buttonHolder;
    }

    public void generateSceneAuto(final Scene scene, final storyInterface intf) {
        switch (scene.type) {
            case STORY:
                generateStoryScene(scene, intf);
                break;
            case IMAGE:
                generateImageScene(scene, intf);
                break;
            case CODE:
                generateCodeScene(scene, intf);
                break;
            default:
                generateStoryScene(scene, intf);
                break;
        }
    }

    private void generateStoryScene(final Scene scene, final storyInterface intf) {
        final MessageFragment messageFragment = new MessageFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(fragmentHolder.getId(), messageFragment);
        transaction.runOnCommit(new Runnable() {
            @Override
            public void run() {
                final TextView mf_tv = messageFragment.getView().findViewById(R.id.message_fragment_tv);
                final StoryContent content = (StoryContent) scene.act.content;
                final int[] pointer = {0};
                mf_tv.setText(content.getMessage(pointer[0]));
                Bar bar = scene.act.bar;
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
                                    intf.nextAct();
                                    mf_tv.setText(content.getMessage(pointer[0]));
                                } else
                                    intf.end();
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

    private void generateImageScene(final Scene scene, final storyInterface intf) {
        final ImageFragment imageFragment = new ImageFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(fragmentHolder.getId(), imageFragment);
        transaction.runOnCommit(new Runnable() {
            @Override
            public void run() {
                final ImageView imageView =
                        imageFragment.getView().findViewById(R.id.image_fragment_imageview);
                final ImageContent content = (ImageContent) scene.act.content;
                final int[] pointer = {0};
                imageView.setImageBitmap(content.getImage(pointer[0]));
                Bar bar = scene.act.bar;
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
                                    intf.nextAct();
                                    imageView.setImageBitmap(content.getImage(pointer[0]));
                                } else
                                    intf.end();
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

    private void generateCodeScene(final Scene scene, final storyInterface intf) {
        final CodeFragment codeFragment = new CodeFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(fragmentHolder.getId(), codeFragment);
        transaction.runOnCommit(new Runnable() {
            @Override
            public void run() {
                final LinearLayout holder =
                        codeFragment.getView().findViewById(R.id.code_fragment_holder);
                final CodeContent content = (CodeContent) scene.act.content;
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
                Bar bar = scene.act.bar;
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
                                intf.end();
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

    public interface storyInterface {
        void nextAct();

        void stop();

        void check(ArrayList<String> code);

        void end();
    }
}
