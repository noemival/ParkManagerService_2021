%====================================================================================
% clientphase description   
%====================================================================================
context(ctxcp, "localhost",  "TCP", "8050").
 qactor( weightsensor, ctxcp, "it.unibo.weightsensor.Weightsensor").
  qactor( outsonar, ctxcp, "it.unibo.outsonar.Outsonar").
  qactor( parkmanagerservice, ctxcp, "it.unibo.parkmanagerservice.Parkmanagerservice").
  qactor( trolley, ctxcp, "it.unibo.trolley.Trolley").
  qactor( client, ctxcp, "it.unibo.client.Client").
