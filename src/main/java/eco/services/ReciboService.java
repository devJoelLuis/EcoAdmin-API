package eco.services;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import eco.dto.ReciboDTO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class ReciboService {
	
	
	
	   // get recibo
		public byte[] relatorio(ReciboDTO rc)  {
			try {

				Map<String, Object> parametros = new HashMap<>();
				parametros.put("TOTAL", rc.getTotal());
				parametros.put("RECEBEMOSDE", rc.getRecebemosde());
				parametros.put("IMPORTANCIA", rc.getImportancia());
				parametros.put("REFERENTE", rc.getReferente());
				parametros.put("DIA", rc.getDia());
				parametros.put("MES", rc.getMes());
				parametros.put("ANO", rc.getAno());
				
				
				
					InputStream inStream = this.getClass().getResourceAsStream("/relatorios/recibo.jasper");
					System.out.println("inStream criado");
					JRDataSource jrdts = new JREmptyDataSource();
					JasperPrint jp = JasperFillManager.fillReport(inStream, parametros, jrdts); //, new JRBeanCollectionDataSource(dados)
					return JasperExportManager.exportReportToPdf(jp);
				
				
			} catch (Exception e) {
				System.out.println(e.getCause());
				System.out.println(e.getMessage());
				return null;
			}
		}

}//fecha classe
