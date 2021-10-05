%====================================================================================
% manager description   
%====================================================================================
context(ctxparkingarea, "localhost",  "TCP", "8050").
 qactor( fan, ctxparkingarea, "it.unibo.fan.Fan").
  qactor( thermometer, ctxparkingarea, "it.unibo.thermometer.Thermometer").
  qactor( parkingmanager, ctxparkingarea, "it.unibo.parkingmanager.Parkingmanager").
