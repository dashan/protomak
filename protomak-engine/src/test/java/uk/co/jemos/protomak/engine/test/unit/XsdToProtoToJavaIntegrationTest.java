package uk.co.jemos.protomak.engine.test.unit;

import java.io.File;

import org.fusesource.hawtbuf.proto.compiler.CompilerException;
import org.fusesource.hawtbuf.proto.compiler.JavaGenerator;
import org.junit.Before;
import org.junit.Test;

import uk.co.jemos.protomak.engine.api.ConversionService;
import uk.co.jemos.protomak.engine.impl.XsomXsdToProtoDomainConversionServiceImpl;
import uk.co.jemos.protomak.engine.test.utils.ProtomakEngineTestConstants;
import uk.co.jemos.protomak.engine.utils.ProtomakEngineHelper;

import com.sun.xml.xsom.parser.XSOMParser;

/**
 * Integration tests to confirm that proto files generated by the
 * {@link ConversionService} can be turned into plain java objects. This in
 * effects proves the validity of the proto files.
 * 
 * @author edwilde
 * 
 */
public class XsdToProtoToJavaIntegrationTest {

	public static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(XsdToProtoToJavaIntegrationTest.class);

	private XsomXsdToProtoDomainConversionServiceImpl service;

	@Before
	public void init() {
		service = new XsomXsdToProtoDomainConversionServiceImpl();
		XSOMParser parser = new XSOMParser();
		service.setParser(parser);
	}

	@Test
	public void simpleSingleElementShouldConvertToAPojo() throws CompilerException {
		service.generateProtoFiles(ProtomakEngineTestConstants.SIMPLE_SINGLE_ELEMENT_XSD_PATH,
				ProtomakEngineTestConstants.PROTOS_OUTPUT_DIR);

		String protoFileName = ProtomakEngineHelper
				.extractProtoFileNameFromXsdName(ProtomakEngineTestConstants.SIMPLE_SINGLE_ELEMENT_FILE_NAME);
		File expectedProtoFile = new File(ProtomakEngineTestConstants.PROTOS_OUTPUT_DIR
				+ File.separatorChar + protoFileName);
		JavaGenerator javaGenerator = new JavaGenerator();
		javaGenerator.setOut(new File(ProtomakEngineTestConstants.POJOS_OUTPUT_DIR));
		try {
			javaGenerator.compile(expectedProtoFile);
		} catch (CompilerException compilerException) {
			for (String error : compilerException.getErrors()) {
				LOG.error(error);
			}

			throw compilerException;
		}
	}
}
