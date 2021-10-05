%====================================================================================
% parkingarea description   
%====================================================================================
context(ctxparkingarea, "localhost",  "TCP", "8021").
 qactor( datacleaner, ctxparkingarea, "rx.dataCleaner").
  qactor( distancefilter, ctxparkingarea, "rx.distanceFilter").
  qactor( basicrobot, ctxparkingarea, "it.unibo.basicrobot.Basicrobot").
  qactor( trolley, ctxparkingarea, "it.unibo.trolley.Trolley").
  qactor( parkmanagerservice, ctxparkingarea, "it.unibo.parkmanagerservice.Parkmanagerservice").
  qactor( parkingservicegui, ctxparkingarea, "it.unibo.parkingservicegui.Parkingservicegui").
