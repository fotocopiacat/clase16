package com.example.android.clase17

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_main.*

//se necesitau un listener, en este caso LOCATIONLISTENER.
// Se debe sobreescribir si o si 4 metodos
class MainActivity : AppCompatActivity(), LocationListener, OnMapReadyCallback {
    //var mapa es objeto de la clase
    var mapa:GoogleMap? = null

    override fun onMapReady(p0: GoogleMap?) {
        //este val mapa se  creo afuera como metodo de clase porque asi es accesible
        mapa = p0
//holadd
        //esto aparece luego de poner ONMAPREADYCALLBACK en la clase
        //esto significa q cuando el git push
        // mapa este listo, se haga algo
        //se debe dar de nuevo los permisos entonces se deben pedir
      //  p0?.isMyLocationEnabled = true
        val permisos = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
        var granted = true
        for (permiso in permisos)
        {
            granted = granted and (ActivityCompat.checkSelfPermission(this, permiso) == PackageManager.PERMISSION_GRANTED)
        }
        if (!granted)
        {
            //aqui se le pone codigo 2 en vez del anterior (el de mas abajo) que dice 1
            ActivityCompat.requestPermissions(this,permisos,2)
        }
        else {
            p0?.isMyLocationEnabled = true
        }
    }

    //necesitamos un locationmanager (asi como los sensormanager)

    var lm : LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //el fragmento mapa necesita un callback q hremos ahora
        val fragmentoMapaCB = supportFragmentManager.findFragmentById(R.id.FragmentMapa) as SupportMapFragment
        fragmentoMapaCB.getMapAsync(this)

        //el gps es un permiso importante entonces debe salir un popup que le indique al usuario
        //sobre el uso del GPS

        val permisos = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
        var granted = true
        for (permiso in permisos)
        {
            granted = granted and (ActivityCompat.checkSelfPermission(this, permiso) == PackageManager.PERMISSION_GRANTED)
        }
        if (!granted)
        {
            ActivityCompat.requestPermissions(this,permisos,1)
        }
        else {
            lm?.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1f,this)
        }

    //fragmento mapa
        val fragmentoMapa = supportFragmentManager.findFragmentById(R.id.FragmentMapa) as SupportMapFragment
        //antes decia THIS con rojo, pro al anadir el listner de mapa (ultimo metodo de main activity_) y
       // sobreescribir, deja de suceder
        fragmentoMapa.getMapAsync(this)
    }

    //estos son los 4 metodos a implementar obligatoriamente
    override fun onLocationChanged(location: Location?) {
        //se asigna textos a los labels del view activity_main
        lblLatitud.text = location?.latitude.toString()
        lblLongitud.text = location?.longitude.toString()
        lblAltitud.text = location?.altitude.toString()
    }

    //se debe hacer ctrol o  para sobrescribir este metodo
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode)
        {
            1->
            {
                lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                var granted = true
                for (permiso in permissions) {
                    granted = granted and (ActivityCompat.checkSelfPermission(
                        this, permiso)== PackageManager.PERMISSION_GRANTED)
                }
                if (grantResults.size > 0 && granted)
                {
                    lm?.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1f,this)
                } else
                {
                    Toast.makeText(this, " permiso de gps requerido",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        //se borra los To DO de los ultimos 3
    }

    override fun onProviderEnabled(provider: String?) {
        //se borra
    }

    override fun onProviderDisabled(provider: String?) {
        //se borra
    }
}