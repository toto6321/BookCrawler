package com.sweettoto.core;

import java.io.File;

import com.sweettoto.util.FileDownloader;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class FileDownloaderPipeline implements Pipeline {
	private String directoryPath = null;

	public FileDownloaderPipeline() {
		this.directoryPath = "Downloads";
	}

	public FileDownloaderPipeline(String directoryPath) {
		this.directoryPath = directoryPath;
	}

	@Override
	public void process(ResultItems resultItems, Task task) {
		if (resultItems.get("COVERURL") != null && !resultItems.get("COVERURL").toString().isEmpty()) {
			File directory = new File(directoryPath);
			// if the directory does not exist, just to create it
			if (!directory.exists()) {
				directory.mkdir();
			}
			// if else, then it exist and then if it isn't a directory, createit,
				// else it is OK and do next.
			else if (!directory.isDirectory()) {
				directory.mkdir();
			}

			String fileURL = resultItems.get("COVERURL").toString();
			FileDownloader fileDownloader = new FileDownloader(fileURL);
			fileDownloader
					.setFilePath(String.format(directory.getAbsolutePath() + "/" + resultItems.get("ISBN")).toString()
							+ fileURL.substring(fileURL.lastIndexOf('.')));
			fileDownloader.downlaodFileFromUrl();
		}
	}

}
