import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String? formattedNumber;

  
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Material App',
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Material App Bar'),
        ),
        body: Center(
          child: Column(
            children: [
              Text('Hello World'),
              Text('The formatted number is ${formattedNumber ?? "No number"}'),
              TextButton(
                onPressed: () async {
                  final numberFormatted = await formatNumber("+573178901234");
                  print(formattedNumber);
                  setState(() {
                    formattedNumber = numberFormatted;
                  });
                },
                child: Text('Format Number'),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Future<String> formatNumber(String number) async {
    if(Platform.isAndroid) {
      final platform = MethodChannel('com.example.app');
      final formattedNumber = await platform.invokeMethod('formatNumber', {'number': number}) as String?;
      return formattedNumber ?? "Error";
    } else {
      return "Error";
    }
  }
}
