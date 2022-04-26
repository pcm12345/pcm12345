1. 설치
maven


        <!-- https://pdfcrowd.com/doc/api/client-library/java/ -->
        <dependency>
            <groupId>com.pdfcrowd</groupId>
            <artifactId>pdfcrowd</artifactId>
            <version>5.5.0</version>
            <scope>compile</scope>
        </dependency>
gradle


compile 'com.pdfcrowd:pdfcrowd:5.5.0'
 

2. 개발
Java Example


import com.pdfcrowd.Pdfcrowd;

    /**
     * PDF 파일 작성 API
     * @param url
     * @param pageWidth
     * @param pageHeight
     * @param viewPortWidth
     * @param viewPortHeight
     * @throws IOException
     * @throws Pdfcrowd.Error
     */
    @GetMapping("/pdf")
    @ApiOperation("PDF파일 만들어주기")
    public ResponseEntity<byte[]> makePDF(@RequestParam("url") String url,
                                          @RequestParam(value="pageWidth", required = false, defaultValue = "8.27in") String pageWidth,
                                          @RequestParam(value="pageHeight", required = false, defaultValue = "11.4in") String pageHeight,
                                          @RequestParam(value="viewPortWidth", required = false, defaultValue = "790") int viewPortWidth,
                                          @RequestParam(value="viewPortHeight", required = false, defaultValue = "1024") int viewPortHeight)
            throws IOException, Pdfcrowd.Error {

        try {
            log.info("### url : " + url);

            String pdfCrowdAPIKey = "affd691000708077e762a7d6cef10aaa";
            String pdfCrowdID = "carrotsds";

            // create the API client instance
            Pdfcrowd.HtmlToPdfClient client =
                    new Pdfcrowd.HtmlToPdfClient(pdfCrowdID, pdfCrowdAPIKey);

            // configure the conversion
            client.setPageWidth(pageWidth);
            client.setPageHeight(pageHeight);
            client.setViewportWidth(viewPortWidth);
            client.setViewportHeight(viewPortHeight);
            client.setSmartScalingMode("content-fit");
            client.setInitialZoomType("fit-width");
            client.setJavascriptDelay(1000);

            // run the conversion and store the result into the "pdf" variable
            byte[] pdf = client.convertUrl(url);

            String fileName = "BnB_Report";

            // API에서 한글 지원 안하는 듯 함
            // set HTTP response headers
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/pdf");
            headers.add("Cache-Control", "max-age=0");
            headers.add("Accept-Ranges", "none");
            headers.add("Content-Disposition", "attachment; filename=\""+fileName+".pdf\"");

            // send the result in the HTTP response
            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        }
        catch(Pdfcrowd.Error why) {
            // send the error in the HTTP response
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "text/plain");
            String msg = String.format("Pdfcrowd Error: %d - %s",
                    why.getCode(), why.getMessage());
            log.info("#### makePDF Error : " + msg);
            return new ResponseEntity<>(msg.getBytes(), headers, HttpStatus.BAD_REQUEST);
        }
    }
 

FrontEnd Example



makePdf(){
    let url = "https://bnb-admin.carrotglobal.com/manager/course_report_print/"+this.courseIdx;
    this.axios.get(`/manager/pdf?url=${url}`,{
        responseType: 'arraybuffer'
    }).then((res)=>{
        var file = new Blob([res.data], {type: 'application/pdf'});
        var fileURL = URL.createObjectURL(file);
        window.open(fileURL);
    }).catch((err)=>{
        console.error(err)
        alert(err);
    });
},
