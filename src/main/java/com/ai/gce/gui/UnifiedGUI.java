package com.ai.gce.gui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.ai.gce.repository.GCERepository;

public class UnifiedGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private final GCERepository repo;
	private final DefaultListModel<String> model;
	private final JList<String> instanceList;

	public UnifiedGUI() {
		super("🧠 AI + Google Compute Engine GUI");

		repo = new GCERepository();
		model = new DefaultListModel<>();
		instanceList = new JList<>(model);

		JButton refreshBtn = new JButton("🔄 Refresh");
		JButton startBtn = new JButton("▶️ Start");
		JButton stopBtn = new JButton("⏹️ Stop");

		refreshBtn.addActionListener(e -> refreshInstances());

		startBtn.addActionListener(e -> {
			String selected = instanceList.getSelectedValue();
			if (selected != null && !selected.isEmpty()) {
				String name = selected.split("\\|")[0].trim();
				String res = repo.startInstance(name);
				JOptionPane.showMessageDialog(this, res);
				refreshInstances();
			}
		});

		stopBtn.addActionListener(e -> {
			String selected = instanceList.getSelectedValue();
			if (selected != null && !selected.isEmpty()) {
				String name = selected.split("\\|")[0].trim();
				String res = repo.stopInstance(name);
				JOptionPane.showMessageDialog(this, res);
				refreshInstances();
			}
		});

		setLayout(new BorderLayout());
		add(new JScrollPane(instanceList), BorderLayout.CENTER);

		JPanel panel = new JPanel();
		panel.add(refreshBtn);
		panel.add(startBtn);
		panel.add(stopBtn);
		add(panel, BorderLayout.SOUTH);

		setSize(700, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		refreshInstances();
	}

	private void refreshInstances() {
		try {
			model.clear();
			List<String> instances = repo.fetchAnalyzedInstances();
			for (String s : instances)
				model.addElement(s);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error fetching instances.");
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new UnifiedGUI().setVisible(true));
	}
}
