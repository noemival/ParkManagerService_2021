%====================================================================================
% parkingarea description   
%====================================================================================
context(ctxparkingarea, "localhost",  "TCP", "8021").
 qactor( fan, ctxparkingarea, "it.unibo.fan.Fan").
  qactor( thermometer, ctxparkingarea, "it.unibo.thermometer.Thermometer").
  qactor( parkmanagerservice, ctxparkingarea, "it.unibo.parkmanagerservice.Parkmanagerservice").
