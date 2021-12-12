%====================================================================================
% parkingarea description   
%====================================================================================
context(ctxparkingarea, "localhost",  "TCP", "8021").
 qactor( datacleaner, ctxparkingarea, "rx.dataCleaner").
  qactor( distancefilter, ctxparkingarea, "rx.distanceFilter").
  qactor( parkmanagerservice, ctxparkingarea, "it.unibo.parkmanagerservice.Parkmanagerservice").
  qactor( trolley, ctxparkingarea, "it.unibo.trolley.Trolley").
  qactor( outmanager, ctxparkingarea, "it.unibo.outmanager.Outmanager").
  qactor( fanmanager, ctxparkingarea, "it.unibo.fanmanager.Fanmanager").
  qactor( parkingmanager, ctxparkingarea, "it.unibo.parkingmanager.Parkingmanager").
  qactor( basicrobot, ctxparkingarea, "it.unibo.basicrobot.Basicrobot").
