%====================================================================================
% parkingarea description   
%====================================================================================
context(ctxparkingarea, "localhost",  "TCP", "8021").
 qactor( datacleaner, ctxparkingarea, "rx.dataCleaner").
  qactor( distancefilter, ctxparkingarea, "rx.distanceFilter").
  qactor( outsonar, ctxparkingarea, "it.unibo.outsonar.Outsonar").
  qactor( outmanager, ctxparkingarea, "it.unibo.outmanager.Outmanager").
  qactor( weightsensor, ctxparkingarea, "it.unibo.weightsensor.Weightsensor").
  qactor( parkmanagerservice, ctxparkingarea, "it.unibo.parkmanagerservice.Parkmanagerservice").
  qactor( trolley, ctxparkingarea, "it.unibo.trolley.Trolley").
  qactor( basicrobot, ctxparkingarea, "it.unibo.basicrobot.Basicrobot").
  qactor( fan, ctxparkingarea, "it.unibo.fan.Fan").
  qactor( thermometer, ctxparkingarea, "it.unibo.thermometer.Thermometer").
  qactor( parkingmanager, ctxparkingarea, "it.unibo.parkingmanager.Parkingmanager").
