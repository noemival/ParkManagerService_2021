%====================================================================================
% parkingarea description   
%====================================================================================
context(ctxparkingarea, "localhost",  "TCP", "8021").
 qactor( datacleaner, ctxparkingarea, "rx.dataCleaner").
  qactor( distancefilter, ctxparkingarea, "rx.distanceFilter").
  qactor( outsonar, ctxparkingarea, "it.unibo.outsonar.Outsonar").
  qactor( weightsensor, ctxparkingarea, "it.unibo.weightsensor.Weightsensor").
  qactor( parkmanagerservice, ctxparkingarea, "it.unibo.parkmanagerservice.Parkmanagerservice").
  qactor( outmanager, ctxparkingarea, "it.unibo.outmanager.Outmanager").
  qactor( trolley, ctxparkingarea, "it.unibo.trolley.Trolley").
  qactor( parkingservicegui, ctxparkingarea, "it.unibo.parkingservicegui.Parkingservicegui").
