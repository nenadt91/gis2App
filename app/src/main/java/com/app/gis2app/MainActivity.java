package com.app.gis2app;

import android.graphics.Color;
import android.graphics.Interpolator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapOptions;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnPinchListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.GeometryUtil;
import com.esri.core.geometry.Line;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.geometry.Transformation2D;
import com.esri.core.map.Graphic;
import com.esri.core.map.SpatialFilter;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.tasks.geocode.LocatorFindParameters;
import com.esri.core.tasks.na.StopGraphic;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Integer> layers = new ArrayList<>();
    List<Point> points = new ArrayList<>();
    MapView mapView;
    Polyline line;
    Polygon polygon = null;
    private TextView marker;
    private TextView poligon;
    private Graphic polygonGraphic;
    private SimpleFillSymbol fillSymbol;
    private SimpleLineSymbol polygonOutline;
    SimpleLineSymbol lineSymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.map);

        marker = (TextView) findViewById(R.id.marker);
        poligon = (TextView) findViewById(R.id.poligon);
        graphicLayer();
        mapView.getCallout().setPassTouchEventsToMapView(true);


        marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.removeLayer(mapView.getLayers().length - 1);

            }
        });
    }


    private void graphicLayer() {
        mapView.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float v, float v1) {

                points.add(mapView.toMapPoint(v, v1));
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.draw:
                new DialogOptions(MainActivity.this).show();
                return true;
            case R.id.undo:
                mapView.removeLayer(mapView.getLayers().length - 1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    int counter = 0;


    public void setDrawingParams(int itemSelection, int typeSelection, int colorSelection) {

        switch (itemSelection) {
            case R.id.line:
                line = new Polyline();
                lineSymbol = new SimpleLineSymbol(getColorSelection(colorSelection), 3, getLineStyle(typeSelection));
                drawLine();
                break;
            case R.id.polyline:
                line = new Polyline();
                lineSymbol = new SimpleLineSymbol(getColorSelection(colorSelection), 3, getLineStyle(typeSelection));
                drawPolylineLine();
                break;
            case R.id.polygon:
                polygon = new Polygon();
                polygonOutline = new SimpleLineSymbol(Color.BLACK, 2, getLineStyle(typeSelection));
                fillSymbol = new SimpleFillSymbol(getColorSelection(colorSelection), SimpleFillSymbol.STYLE.SOLID);
                fillSymbol.setOutline(polygonOutline);
                drawPolygon();
                break;
        }

    }

    private void drawPolylineLine() {
        line.startPath(points.get(0).getX(), points
                .get(0).getY());
        for (int i = 1; i < points.size(); i++) {
            line.lineTo(points.get(i).getX(), points
                    .get(i).getY());
        }
        polygonGraphic = new Graphic(line, lineSymbol);
        GraphicsLayer graphicsLayer = new GraphicsLayer();
        graphicsLayer.addGraphic(polygonGraphic);
        mapView.addLayer(graphicsLayer);
    }


    private void drawLine() {
        line.startPath(points.get(0).getX(), points
                .get(0).getY());
        line.lineTo(points.get(1).getX(), points
                .get(1).getY());
        polygonGraphic = new Graphic(line, lineSymbol);
        GraphicsLayer graphicsLayer = new GraphicsLayer();
        graphicsLayer.addGraphic(polygonGraphic);
        mapView.addLayer(graphicsLayer);
    }

    private SimpleLineSymbol.STYLE getLineStyle(int typeSelection) {

        switch (typeSelection) {
            case R.id.solid:
                return SimpleLineSymbol.STYLE.SOLID;
            case R.id.dash_dot:
                return SimpleLineSymbol.STYLE.DASHDOT;
            case R.id.dash_dot_dot:
                return SimpleLineSymbol.STYLE.DASHDOTDOT;
            case R.id.dash:
                return SimpleLineSymbol.STYLE.DASH;
        }
        return SimpleLineSymbol.STYLE.SOLID;
    }

    private int getColorSelection(int colorSelection) {
        switch (colorSelection) {
            case R.id.red:
                return Color.RED;
            case R.id.blue:
                return Color.BLUE;
            case R.id.yellow:
                return Color.YELLOW;
            case R.id.green:
                return Color.GREEN;

        }
        return 0;
    }

    private void drawPolygon() {

        polygon.startPath(points.get(0).getX(), points
                .get(0).getY());
        for (int i = 1; i < points.size(); i++) {
            polygon.lineTo(points.get(i).getX(), points
                    .get(i).getY());
        }
        polygonGraphic = new Graphic(polygon, fillSymbol);
        GraphicsLayer graphicsLayer = new GraphicsLayer();
        graphicsLayer.addGraphic(polygonGraphic);
        mapView.addLayer(graphicsLayer);
    }
}
