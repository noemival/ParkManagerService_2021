%====================================================================================
% parkingarea description   
%====================================================================================
context(ctxparkingarea, "localhost",  "TCP", "8021").
 qactor( datacleaner, ctxparkingarea, "rx.dataCleaner").
  qactor( distancefilter, ctxparkingarea, "rx.distanceFilter").
  qactor( parkmanagerservice, ctxparkingarea, "it.unibo.parkmanagerservice.Parkmanagerservice").
  qactor( outmanager, ctxparkingarea, "it.unibo.outmanager.Outmanager").
  qactor( trolley, ctxparkingarea, "it.unibo.trolley.Trolley").
  qactor( thermometer, ctxparkingarea, "it.unibo.thermometer.Thermometer").
  qactor( parkingmanager, ctxparkingarea, "it.unibo.parkingmanager.Parkingmanager").
  qactor( parkservicestatusgui, ctxparkingarea, "it.unibo.parkservicestatusgui.Parkservicestatusgui").
  qactor( fan, ctxparkingarea, "it.unibo.fan.Fan").
  qactor( basicrobot, ctxparkingarea, "it.unibo.basicrobot.Basicrobot").
