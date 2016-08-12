
Chess.java - Main Program with main class in it.
ChessUI - User Interface and Mouse commands, more the design side of project

ChessPieces.png  [online]-https://onedrive.live.com/?cid=d4629bc8d856f7d5&id=D4629BC8D856F7D5%21137&authkey=!AEOefAPxykxoZx8

        Image backgroundImage;
        backgroundImage = new ImageIcon("Cream.jpg").getImage();
        g.drawImage(backgroundImage, 0, 0, this);
                this.addMouseMotionListener(this);
        Image darkImage;
        darkImage = new ImageIcon("Dark.jpg").getImage();
        g.drawImage(darkImage, 0, 0, this);

        Image lightImage;
        lightImage = new ImageIcon("Light.jpg").getImage();
        g.drawImage(lightImage, 0, 0, this);
        
        this.addMouseMotionListener(this);
        
        int everySecondRow=0;
        for(int i=0; i<4;i++){
            g.drawImage(darkImage, 0, everySecondRow, this);
            g.drawImage(lightImage, 60, everySecondRow, this);
            g.drawImage(darkImage, 120, everySecondRow, this);
            g.drawImage(lightImage, 180, everySecondRow, this);
            g.drawImage(darkImage, 240, everySecondRow, this);
            g.drawImage(lightImage, 300, everySecondRow, this);
            g.drawImage(darkImage, 360, everySecondRow, this);
            g.drawImage(lightImage, 420, everySecondRow, this);
            everySecondRow+=120;
        }
        
        everySecondRow=0;
        
        for(int i=0; i<4;i++){
            everySecondRow+=60;
            g.drawImage(lightImage, 0, everySecondRow, this);
            g.drawImage(darkImage, 60, everySecondRow, this);
            g.drawImage(lightImage, 120, everySecondRow, this);
            g.drawImage(darkImage, 180, everySecondRow, this);
            g.drawImage(lightImage, 240, everySecondRow, this);
            g.drawImage(darkImage, 300, everySecondRow, this);
            g.drawImage(lightImage, 360, everySecondRow, this);
            g.drawImage(darkImage, 420, everySecondRow, this);
            everySecondRow+=60;
        }