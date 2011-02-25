<?php
include("lastVersion.php");
header("Expires: Mon, 26 Jul 1997 05:00:00 GMT"); // Date in the past
header("Last-Modified: " . gmdate("D, d M Y H:i:s") . " GMT");
header("Cache-Control: no-store, no-cahce, must-revalidate");
header("Pragma: no-cache");
header("Content-Type: application/x-java-jnlp-file; name=KevoreeRuntime.jnlp");
header("Content-disposition: attachment; filename=KevoreeRuntime.jnlp");
$version = "1.0.0-RC3";
echo "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
?>
<jnlp spec="1.0" codebase="http://dist.kevoree.org/" href="KevoreeRuntime.php" >
   <information>
      <title>Kevoree Runtime</title>
      <vendor>INRIA</vendor>
      <icon href="icon/kev-logo-full.png" width="64" height="64"/>
	  <offline-allowed />
	  <association mime-type="application-x/kevmodel" extensions="kev"/>
   </information>
   <resources>
      <?php echo "<jar href=\"http://maven.kevoree.org/libs-release-local/org/kevoree/platform/org.kevoree.platform.osgi.standalone.gui/$kevVersion/org.kevoree.platform.osgi.standalone.gui-$kevVersion.jar\" main=\"true\" />"; ?>
   </resources>
   <application-desc main-class="org.kevoree.platform.osgi.standalone.gui.App"/>
   <security>
     <all-permissions/>
  </security>
</jnlp>