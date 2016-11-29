package com.elfec.ssc.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.model.Usage;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

public class ViewUsageAdapter extends ArrayAdapter<Usage> {
	private List<Usage> usage;
	private int resource;
	private int maxUsage;
	private LayoutInflater inflater = null;
	private NumberFormat nf;

	public ViewUsageAdapter(Context context, int resource,
			final List<Usage> usage) {
		super(context, resource, usage);
		this.usage = usage;
		this.resource = resource;
		this.maxUsage = maxConsume(usage);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		nf = DecimalFormat.getInstance();
		DecimalFormatSymbols customSymbol = new DecimalFormatSymbols();
		customSymbol.setDecimalSeparator(',');
		customSymbol.setGroupingSeparator('.');
		((DecimalFormat)nf).setDecimalFormatSymbols(customSymbol);
		nf.setGroupingUsed(true);
	}
	
	/**
	 * Obtiene el máximo consumo de todos de la lista de consumos
	 * @param usages
	 * @return máximo consumo
	 */
	private int maxConsume(List<Usage> usages)
	{
		int max = 0;
		for (Usage usage : usages) {
			max = Math.max(max, usage.getEnergyUsage());
		}
		return max;
	}
	
	

	@Override
	public int getCount() {
		return usage.size();
	}

	@Override
	public Usage getItem(int position) {
		return usage.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null)
			convertView = inflater.inflate(resource, null, false);
		Usage u = usage.get(position);
		((TextView) convertView.findViewById(R.id.term_text)).setText(u.getTerm());
		ProgressBar progress = ((ProgressBar) convertView.findViewById(R.id.progressBar1));
		progress.setMax(maxUsage);
		progress.setProgress(u.getEnergyUsage());

		((TextView) convertView.findViewById(R.id.usage_text))
		.setText(""+u.getEnergyUsage());
	
		return convertView;
	}

	
	
}
