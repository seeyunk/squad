import java.io.File;

import net.lingala.zip4j.core.ZipFile;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squad.manager.SquadServiceManager;


public class CompressTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void test() throws Exception {
		String zipFile = "/home/seeyunk/temp/scout-1/squad-sysroute-eam.zip";
		
		SquadServiceManager sm = new SquadServiceManager( null );
		sm.unzipService( new File( zipFile ) );
	}
}
