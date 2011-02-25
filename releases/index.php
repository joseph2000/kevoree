<?php include("lastVersion.php"); ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

	<title>dukeboard/kevoree @ GitHub</title>
	
	<style type="text/css">
		body {
  		margin-top: 1.0em;
  		background-color: #3F80BB;
		  font-family: "Helvetica,Arial,FreeSans";
  		color: #FFFFFF;
    }
    #container {
      margin: 0 auto;
      width: 800px;
    }
		h1 { font-size: 3.8em; color: #FFC827; margin-bottom: 3px; }
		h1 .small { font-size: 0.4em; }
		h1 a { text-decoration: none }
		h2 { font-size: 1.5em; color: #FFC827; }
    h3 { text-align: center; color: #ba8609; }
    a { color: #FFC827; }
    .description { font-size: 1.2em; margin-bottom: 30px; margin-top: 30px; font-style: italic;}
    .download { float: right; }
		pre { background: #000; color: #fff; padding: 15px;}
    hr { border: 0; width: 80%; border-bottom: 1px solid #aaa}
    .footer { text-align:center; padding-top:30px; font-style: italic; }
	</style>
	
</head>

<body>
  <a href="http://github.com/dukeboard/kevoree"><img style="position: absolute; top: 0; right: 0; border: 0;" src="http://s3.amazonaws.com/github/ribbons/forkme_right_darkblue_121621.png" alt="Fork me on GitHub" /></a>

  <div id="container">


    <div class="download">
      <a href="http://github.com/dukeboard/kevoree/zipball/master">
        <img border="0" width="90" src="http://github.com/images/modules/download/zip.png"></a>
      <a href="http://github.com/dukeboard/kevoree/tarball/master">
        <img border="0" width="90" src="http://github.com/images/modules/download/tar.png"></a>
    </div>

    <h1><a href="http://github.com/dukeboard/kevoree"><img border="0" src="icon/kevoree-logo-full.png" alt="Kevoree" /></a>
      <span class="small">by <a href="http://github.com/dukeboard">dukeboard</a></span></h1>

    <div class="description">
      Kevoree component model
    </div>

    <h2>Authors</h2>
<p>Fouquet François (fouquet.f@gmail.com)<br/><br/>      </p>
<h2>Contact</h2>
<p>Fouquet François (fouquet.f@gmail.com)<br/>      </p>


    <h2>Download - version=<? echo $kevVersion; ?></h2>
    <p>
      You can download this project in either
      <a href="http://github.com/dukeboard/kevoree/zipball/master">zip</a> or
      <a href="http://github.com/dukeboard/kevoree/tarball/master">tar</a> formats.
    </p>
    
    <p>You can run Kevoree editor via JNLP link
    	<a href="http://dist.kevoree.org/KevoreeEditor.php">Kevoree Editor (Java Web Start)<img src="icon/kev-logo-small.png" /></a>
    </p>
    <p>You can run Kevoree Runtime via JNLP link
    	<a href="http://dist.kevoree.org/KevoreeRuntime.php">Kevoree Runtime (Java Web Start)<img src="icon/kev-logo-small.png" /></a>
    </p>
    <p>You can download Kevoree Android Runtime (apk) via this link
    <?php echo "<a href=\"http://maven.kevoree.org/libs-release-local/org/kevoree/platform/org.kevoree.platform.osgi.android/$kevVersion/org.kevoree.platform.osgi.android-$kevVersion.apk\">Kevoree Android Runtime (apk)<img src=\"icon/kev-logo-small.png\" /></a>"; ?>

    </p>
    
    
    <p>You can also clone the project with <a href="http://git-scm.com">Git</a>
      by running:
      <pre>$ git clone git://github.com/dukeboard/kevoree</pre>
    </p>

    <div class="footer">
      get the source code on GitHub : <a href="http://github.com/dukeboard/kevoree">dukeboard/kevoree</a>
    </div>

  </div>

  
</body>
</html>
