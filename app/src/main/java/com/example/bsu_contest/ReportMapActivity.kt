package com.example.bsu_contest

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider


private lateinit var mapView: MapView

class ReportMapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* Инициализируем яндекс MapKit */
        MapKitFactory.initialize(this)

        /* т.к. Яндекс MpKit не содержит методов для Jetpack Compose показываем xml */
        setContentView(R.layout.activity_report_screen)
        mapView = findViewById(R.id.mapview)

        /* Задаем место в котором появимся на карте */
        mapView.mapWindow.map.move(
            CameraPosition(
                Point(
                    intent.extras?.getDouble("latitude")!!,
                    intent.extras?.getDouble("longitude")!!
                ),
                /* zoom = */ 17.0f,
                /* azimuth = */ 0.0f,
                /* tilt = */ 30.0f
            )
        )

        /* Ставим метку на карте */
        var imageProvider = ImageProvider.fromResource(this, R.drawable.pointer0_25x)
        var placemark = mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(
                intent.extras?.getDouble("latitude")!!,
                intent.extras?.getDouble("longitude")!!
            )
            setIcon(imageProvider)
        }

        /* Попытка отобразить местоположение пользователя */
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),0)
        var mapKit = MapKitFactory.getInstance()
        var locationMapKit = mapKit.createUserLocationLayer(mapView.mapWindow)
        locationMapKit.isVisible = true
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}