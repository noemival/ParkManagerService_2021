%====================================================================================
% thermometer description   
%====================================================================================
context(ctxthermometer, "localhost",  "TCP", "8015").
context(ctxparkingarea, "127.0.0.1",  "TCP", "8021").
 qactor( parkingmanager, ctxparkingarea, "external").
  qactor( thermometer, ctxthermometer, "it.unibo.thermometer.Thermometer").
  qactor( mockthermometer, ctxthermometer, "it.unibo.mockthermometer.Mockthermometer").
