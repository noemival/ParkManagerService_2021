%====================================================================================
% parkingarea description   
%====================================================================================
context(ctxparkingarea, "localhost",  "TCP", "8021").
 qactor( datacleaner, ctxparkingarea, "rx.dataCleaner").
  qactor( distancefilter, ctxparkingarea, "rx.distanceFilter").
  qactor( parkingservicegui, ctxparkingarea, "it.unibo.parkingservicegui.Parkingservicegui").
  qactor( parkservicestatusgui, ctxparkingarea, "it.unibo.parkservicestatusgui.Parkservicestatusgui").
  qactor( outsonar, ctxparkingarea, "it.unibo.outsonar.Outsonar").
  qactor( weightsensor, ctxparkingarea, "it.unibo.weightsensor.Weightsensor").
  qactor( fan, ctxparkingarea, "it.unibo.fan.Fan").
  qactor( thermometer, ctxparkingarea, "it.unibo.thermometer.Thermometer").
  qactor( parkmanagerservice, ctxparkingarea, "it.unibo.parkmanagerservice.Parkmanagerservice").
  qactor( outmanager, ctxparkingarea, "it.unibo.outmanager.Outmanager").
  qactor( parkingmanager, ctxparkingarea, "it.unibo.parkingmanager.Parkingmanager").
  qactor( trolley, ctxparkingarea, "it.unibo.trolley.Trolley").
  qactor( basicrobot, ctxparkingarea, "it.unibo.basicrobot.Basicrobot").
