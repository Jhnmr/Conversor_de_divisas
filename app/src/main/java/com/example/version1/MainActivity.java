package com.example.version1;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private EditText inputCantidad;
    private TextView resultadoText;
    private Spinner spinnerMonedaOrigen, spinnerMonedaDestino;
    private Button btnConvertir;

    // Mapa de tasas de cambio
    private final HashMap<String, Double> tasasDeCambio = new HashMap<String, Double>() {{
        put("USD", 1.0);
        put("CRC", 535.50);
        put("NIO", 36.50);
        put("PHP", 55.80);
        put("EUR", 0.92);
    }};

    // Nombres completos de las monedas
    private final HashMap<String, String> nombresMonedas = new HashMap<String, String>() {{
        put("USD", "Dólar estadounidense (USD)");
        put("CRC", "Colón costarricense (CRC)");
        put("NIO", "Córdoba nicaragüense (NIO)");
        put("PHP", "Peso filipino (PHP)");
        put("EUR", "Euro (EUR)");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Conectar elementos de la interfaz
        inputCantidad = findViewById(R.id.inputCantidad);
        resultadoText = findViewById(R.id.resultadoText);
        spinnerMonedaOrigen = findViewById(R.id.spinnerMonedaOrigen);
        spinnerMonedaDestino = findViewById(R.id.spinnerMonedaDestino);
        btnConvertir = findViewById(R.id.btnConvertir);

        // Configurar Spinners con nombres completos
        String[] monedas = {"USD", "CRC", "NIO", "PHP", "EUR"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, monedas);
        spinnerMonedaOrigen.setAdapter(adapter);
        spinnerMonedaDestino.setAdapter(adapter);

        // Manejar el evento de clic en el botón
        btnConvertir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertirMoneda();
            }
        });

        // Establecer valor predeterminado en el campo de cantidad
        inputCantidad.setText("1.0");
    }

    private void convertirMoneda() {
        try {
            // Obtener la cantidad ingresada
            String cantidadStr = inputCantidad.getText().toString();
            if (cantidadStr.isEmpty()) {
                resultadoText.setText("Por favor, ingrese una cantidad");
                return;
            }

            double cantidad = Double.parseDouble(cantidadStr);
            if (cantidad <= 0) {
                resultadoText.setText("La cantidad debe ser un valor positivo");
                return;
            }

            String monedaOrigen = spinnerMonedaOrigen.getSelectedItem().toString();
            String monedaDestino = spinnerMonedaDestino.getSelectedItem().toString();

            // Evitar conversión entre la misma moneda
            if (monedaOrigen.equals(monedaDestino)) {
                resultadoText.setText("La moneda de origen y destino son iguales");
                return;
            }

            double resultado = convertir(cantidad, monedaOrigen, monedaDestino);
            // Mostrar el resultado de forma más clara con nombres completos de monedas
            resultadoText.setText(String.format("Resultado: %.2f %s\nCantidad original: %.2f %s", resultado, nombresMonedas.get(monedaDestino), cantidad, nombresMonedas.get(monedaOrigen)));
        } catch (NumberFormatException e) {
            resultadoText.setText("Por favor, ingrese un número válido");
        }
    }

    private double convertir(double cantidad, String origen, String destino) {
        double usd = convertirAUSD(cantidad, origen);
        return convertirDesdeUSD(usd, destino);
    }

    private double convertirAUSD(double cantidad, String origen) {
        return cantidad / tasasDeCambio.get(origen);
    }

    private double convertirDesdeUSD(double cantidad, String destino) {
        return cantidad * tasasDeCambio.get(destino);
    }
}
