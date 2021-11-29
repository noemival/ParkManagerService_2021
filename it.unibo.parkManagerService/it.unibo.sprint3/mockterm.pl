%====================================================================================
% mockterm description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "thermometer/data").
context(ctxthermometer, "localhost",  "TCP", "8023").
 qactor( mockthermometer, ctxthermometer, "it.unibo.mockthermometer.Mockthermometer").
