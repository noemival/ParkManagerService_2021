%====================================================================================
% sonaronrasp description   
%====================================================================================
context(ctxsonaronrasp, "localhost",  "TCP", "8070").
context(ctxparkingarea, "192.168.181.19",  "TCP", "8021").
 qactor( outmanager, ctxparkingarea, "external").
  qactor( sonarsimulator, ctxsonaronrasp, "sonarSimulator").
  qactor( sonardatasource, ctxsonaronrasp, "sonarHCSR04Support2021").
  qactor( datacleaner, ctxsonaronrasp, "dataCleaner").
  qactor( sonar, ctxsonaronrasp, "it.unibo.sonar.Sonar").
tracing.
