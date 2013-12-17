package com.mengdd.min3d;

import min3d.Shared;
import min3d.core.Object3dContainer;
import min3d.core.Renderer;
import min3d.core.Scene;
import min3d.interfaces.ISceneController;
import min3d.parser.IParser;
import min3d.parser.Parser;
import min3d.vos.Light;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;

import com.mengdd.arapp.R;
import com.mengdd.components.ViewModel;
import com.mengdd.utils.LogUtils;

public class Min3DLayerViewModel extends ViewModel implements ISceneController {
    private View mRootView = null;

    private Object3dContainer object = null;
    private GLSurfaceView mGlSurfaceView = null;
    private Scene mScene = null;
    private Renderer mMin3dRenderer = null;
    protected Handler mInitSceneHandler;
    protected Handler mUpdateSceneHandler;

    final Runnable _initSceneRunnable = new Runnable() {
        public void run() {
            onInitScene();
        }
    };

    final Runnable _updateSceneRunnable = new Runnable() {
        public void run() {
            onUpdateScene();
        }
    };

    public Min3DLayerViewModel(Activity activity) {
        super(activity);
    }

    @Override
    public View getView() {
        return mRootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRootView = mInflater.inflate(R.layout.min3d_viewmodel, null);

        initRenderer();

        ((FrameLayout) mRootView).addView(mGlSurfaceView);
    }

    private void initRenderer() {
        mInitSceneHandler = new Handler();
        mUpdateSceneHandler = new Handler();

        Shared.context(mActivity);
        mScene = new Scene(this);

        mMin3dRenderer = new Renderer(mScene);
        Shared.renderer(mMin3dRenderer);

        mGlSurfaceView = new GLSurfaceView(mActivity);

        // make the surface transparent
        mGlSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mGlSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);

        mGlSurfaceView.setRenderer(mMin3dRenderer);
        mGlSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

    }

    @Override
    public void initScene() {
        mScene.lights().add(new Light());
        // mScene.backgroundColor().setAll(0x00000000);//set the background
        // color to transparent
        try {

            IParser parser = Parser.createParser(Parser.Type.OBJ,
                    mActivity.getResources(), "com.mengdd.arapp:raw/cube1",
                    false);

            parser.parse();

            object = parser.getParsedObject();
            object.scale().x = 0.1f;
            object.scale().y = 0.1f;
            object.scale().z = 0.1f;
            // object.position().x = 5;//-left +right
            // object.position().y = 5;//-low + high
            object.position().z = -10;// -far +near
            mScene.addChild(object);

            mScene.backgroundColor().setAll(255, 255, 255, 0);
        }
        catch (Exception e) {
            LogUtils.e("init error in min3d, maybe the model file problem");
        }

    }

    @Override
    public void updateScene() {

        if (null != object) {
            object.rotation().x++;
            object.rotation().z++;
        }

    }

    /**
     * Called _after_ scene init (ie, after initScene). Unlike initScene(), gets
     * called from the UI thread.
     */
    public void onInitScene() {
    }

    /**
     * Called _after_ updateScene() Unlike initScene(), gets called from the UI
     * thread.
     */
    public void onUpdateScene() {
    }

    @Override
    public Handler getInitSceneHandler() {
        return mInitSceneHandler;
    }

    @Override
    public Runnable getInitSceneRunnable() {
        return _initSceneRunnable;
    }

    @Override
    public Handler getUpdateSceneHandler() {
        return mUpdateSceneHandler;
    }

    @Override
    public Runnable getUpdateSceneRunnable() {
        return _updateSceneRunnable;
    }

    @Override
    public void onResume(Intent intent) {
        super.onResume(intent);
        mGlSurfaceView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mGlSurfaceView.onPause();
    }
}
