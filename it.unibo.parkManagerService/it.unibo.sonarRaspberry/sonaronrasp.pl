%====================================================================================
% sonaronrasp description   
%====================================================================================
context(ctxsonaronrasp, "localhost",  "TCP", "8070").
context(ctxparkingarea, "127.0.0.1",  "TCP", "8021").
 qactor( outmanager, ctxparkingarea, "external").
  qactor( sonarsimulator, ctxsonaronrasp, "sonarSimulator").
  qactor( sonardatasource, ctxsonaronrasp, "sonarHCSR04Support2021").
  qactor( datacleaner, ctxsonaronrasp, "dataCleaner").
  qactor( sonar, ctxsonaronrasp, "it.unibo.sonar.Sonar").
tracing.
