package com.seu.edu.search.message;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.seu.edu.common.pojo.SearchItem;
import com.seu.edu.search.mapper.ItemMapper;

public class ItemAddMessageListener implements MessageListener {

	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;
	
	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			Long itemId = new Long(text);
			
			Thread.sleep(100);
			
			SearchItem serachItem = itemMapper.getItemById(itemId);
			SolrInputDocument solrDocument = new SolrInputDocument();
			solrDocument.addField("id", serachItem.getId());
			solrDocument.addField("item_title", serachItem.getTitle());
			solrDocument.addField("item_sell_point", serachItem.getSell_point());
			solrDocument.addField("item_price", serachItem.getPrice());
			solrDocument.addField("item_image", serachItem.getImage());
			solrDocument.addField("item_category_name", serachItem.getCategory_name());
			
			solrServer.add(solrDocument);
			solrServer.commit();
			
		} catch (JMSException | SolrServerException | IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
