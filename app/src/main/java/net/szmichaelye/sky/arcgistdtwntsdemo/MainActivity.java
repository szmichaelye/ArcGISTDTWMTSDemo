package net.szmichaelye.sky.arcgistdtwntsdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.esri.arcgisruntime.data.TileCache;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.ImageTiledLayer;
import com.esri.arcgisruntime.layers.WebTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG="MainActivity";
    private MapView mapView;
    private WebTiledLayer webTiledLayer;
    private WebTiledLayer webTiledLayerAnno;
    private GraphicsOverlay mGraphicsOverlay;
    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION
            , Manifest.permission.ACCESS_COARSE_LOCATION
            , Manifest.permission.INTERNET//请求网络权限
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView=(MapView)findViewById(R.id.mmapview);
        quanxian();


        TileCache tileCache = new TileCache("/sdcard/Download/NNROAD01/Layers");
        ArcGISTiledLayer tiledLayer = new ArcGISTiledLayer(tileCache);

        webTiledLayer = TdtMapLayer.CreateTianDiTuTiledLayer(TdtMapLayer.LayerType.TIANDITU_IMAGE_2000);
        webTiledLayerAnno = TdtMapLayer.CreateTianDiTuTiledLayer(TdtMapLayer.LayerType.TIANDITU_IMAGE_ANNOTATION_CHINESE_2000);
        final List<ImageTiledLayer> imageBaseMapLayerList = new ArrayList<>();
        imageBaseMapLayerList.add(webTiledLayer);
        imageBaseMapLayerList.add(webTiledLayerAnno);
        imageBaseMapLayerList.add(tiledLayer);

        webTiledLayer.loadAsync();
        ArcGISMap map = new ArcGISMap(new Basemap());
        mapView.setMap(map);
        map.getOperationalLayers().addAll(imageBaseMapLayerList);

        webTiledLayer.loadAsync();
        webTiledLayerAnno.loadAsync();
        tiledLayer.loadAsync();

    }



    private void quanxian() {

        List<String> mPermissionList = new ArrayList<>();
        /**
         * 判断哪些权限未授予
         */
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }

        /**
         * 判断是否为空
         */
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }
    }
}
