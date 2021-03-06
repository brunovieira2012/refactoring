package br.com.duplabrunodenise.nf;

import java.util.ArrayList;

import br.com.duplabrunodenise.nf.entities.NotaFiscal;
import br.com.duplabrunodenise.nf.services.BancoService;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;

@SuppressWarnings("serial")
public class CIndex extends GenericForwardComposer {
	private DataBinder binder;
	private Textbox txtbxNomeCliente;
	private Decimalbox dcmlbxValorFatura;
	private Radiogroup rdgrpTipoImposto;
	private Listbox lstbxNotaFiscal;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		binder = new AnnotateDataBinder(comp);
		binder.loadAll();
	}

	public ArrayList<NotaFiscal> getListaTodasNotasFiscaisDoBanco() {
		return (ArrayList<NotaFiscal>) BancoService.findAllEntities(NotaFiscal.class);
	}

	public void onClick$btnGerarNotaFiscal() {
		Clients.showBusy("Gerando nota fiscal");
		Events.echoEvent("onGerarNotaFiscal", this.self, null);
	}

	public void onGerarNotaFiscal() {
		Fatura fatura = new Fatura(this.txtbxNomeCliente.getValue(), this.dcmlbxValorFatura.getValue().doubleValue());
		ImpostoInterface imposto;

		if (this.rdgrpTipoImposto.getSelectedItem().getValue().toString().compareTo("1") == 0) {
			imposto = new ImpostoISS();
		} else {
			imposto = new ImpostoICMS();
		}

		NotaFiscal notaFiscal = new GeradorNotaFiscal().geraNota(fatura, imposto);

		this.binder.loadComponent(this.lstbxNotaFiscal);
		this.lstbxNotaFiscal.setVisible(true);
		Clients.clearBusy();

		Messagebox.show("Nota Fiscal gerada com sucesso");
	}
}
