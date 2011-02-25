<?php
include("lastVersion.php");
header("Expires: Mon, 26 Jul 1997 05:00:00 GMT"); // Date in the past
header("Last-Modified: " . gmdate("D, d M Y H:i:s") . " GMT");
header("Cache-Control: no-store, no-cahce, must-revalidate");
header("Pragma: no-cache");
header("Content-Type: application/x-java-jnlp-file; name=KevoreeEditor.jnlp");
header("Content-disposition: attachment; filename=KevoreeEditor.jnlp");
$version = "1.0.0-RC3";
echo "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
?>
<jnlp spec="1.0" codebase="http://dist.kevoree.org/" href="KevoreeEditor.php" >
   <information>
      <title>Kevoree Editor</title>
      <vendor>INRIA</vendor>
      <icon href="icon/kev-logo-full.png" width="64" height="64"/>
	  <offline-allowed />
	  <association mime-type="application-x/kevmodel" extensions="kev"/>
   </information>
   <resources>
      <?php echo "<jar href=\"http://maven.kevoree.org/libs-release-local/org/kevoree/tools/org.kevoree.tools.ui.editor.standalone/$kevVersion/org.kevoree.tools.ui.editor.standalone-$kevVersion.jar\" main=\"true\" />"; ?>
   </resources>
   <application-desc main-class="org.kevoree.tools.ui.editor.standalone.App"/>
   <security>
     <all-permissions/>
  </security>
</jnlp>