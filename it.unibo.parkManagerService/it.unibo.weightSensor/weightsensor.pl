%====================================================================================
% weightsensor description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "weightsensor/data").
context(ctxweightsensor, "localhost",  "TCP", "8018").
 qactor( weightsensor, ctxweightsensor, "it.unibo.weightsensor.Weightsensor").
