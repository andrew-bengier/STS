package SOLTS;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.TooManyListenersException;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.JComponent;

public class FileDrop {
	private transient Border normalBorder;
    private transient DropTargetListener dropListener;
    
    private static Boolean supportsDnD;
    private static Color defaultBorderColor = new Color( 0f, 0f, 1f, 0.25f );
    
    public FileDrop(final Component c, final Listener listener) {
    	this( null, c, BorderFactory.createMatteBorder( 2, 2, 2, 2, defaultBorderColor ), true, listener );
    }
    
    public FileDrop(final Component c, final boolean recursive, final Listener listener) {
    	this( null, c, BorderFactory.createMatteBorder( 2, 2, 2, 2, defaultBorderColor ), recursive, listener );
    }
    
    public FileDrop(final PrintStream out, final Component c, final Listener listener) {
    	this( out, c, BorderFactory.createMatteBorder( 2, 2, 2, 2, defaultBorderColor ), false, listener );
    }
    
    public FileDrop(final PrintStream out, final Component c, final boolean recursive, final Listener listener) {
    	this( out, c, BorderFactory.createMatteBorder( 2, 2, 2, 2, defaultBorderColor ), recursive, listener );
    }
    
    public FileDrop( final Component c, final Border dragBorder, final Listener listener) {  
    	this( null, c, dragBorder, false, listener );
    }
    
    public FileDrop( final Component c, final Border dragBorder, final boolean recursive, final Listener listener) {
    	this( null, c, dragBorder, recursive, listener );
    }
    
    public FileDrop( final PrintStream out, final Component c, final Border dragBorder, final Listener listener) {
    	this( out, c, dragBorder, false, listener );
    }
    
    public FileDrop( final PrintStream out, final Component c, final Border dragBorder, final boolean recursive, final Listener listener) {   
        if( supportsDnD() ) {
            dropListener = new DropTargetListener() {
            	public void dragEnter( DropTargetDragEvent evt ) { 
            		log( out, "FileDrop: dragEnter event." );

                    if( isDragOk( out, evt ) ) {
                        if( c instanceof JComponent ) {   
                        	JComponent jc = (JComponent) c;
                            normalBorder = jc.getBorder();
                            log( out, "FileDrop: normal border saved." );
                            jc.setBorder( dragBorder );
                            log( out, "FileDrop: drag border set." );
                        }

                        evt.acceptDrag( DnDConstants.ACTION_COPY );
                        log( out, "FileDrop: event accepted." );
                    }
                    else {
                        evt.rejectDrag();
                        log( out, "FileDrop: event rejected." );
                    }
                }

                public void dragOver( DropTargetDragEvent evt ) {}

                public void drop( DropTargetDropEvent evt ) {
                	log( out, "FileDrop: drop event." );
                    
                	try {   
                		Transferable tr = evt.getTransferable();

                        if (tr.isDataFlavorSupported (DataFlavor.javaFileListFlavor)) {
                            evt.acceptDrop ( DnDConstants.ACTION_COPY );
                            log( out, "FileDrop: file list accepted." );

                            List fileList = (List) tr.getTransferData(DataFlavor.javaFileListFlavor);
                            Iterator iterator = fileList.iterator();

                            File[] filesTemp = new File[ fileList.size() ];
                            fileList.toArray( filesTemp );
                            final File[] files = filesTemp;

                            if( listener != null ) {
                                listener.filesDropped( files );
                            }

                            evt.getDropTargetContext().dropComplete(true);
                            log( out, "FileDrop: drop complete." );
                        }
                        else {
                            DataFlavor[] flavors = tr.getTransferDataFlavors();
                            boolean handled = false;
                            
                            for (int zz = 0; zz < flavors.length; zz++) {
                                if (flavors[zz].isRepresentationClassReader()) {
                                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                                    log(out, "FileDrop: reader accepted.");

                                    Reader reader = flavors[zz].getReaderForText(tr);

                                    BufferedReader br = new BufferedReader(reader);
                                    
                                    if(listener != null)
                                        listener.filesDropped(createFileArray(br, out));

                                    evt.getDropTargetContext().dropComplete(true);
                                    log(out, "FileDrop: drop complete.");
                                    handled = true;
                                    break;
                                }
                            }
                            if(!handled){
                                log( out, "FileDrop: not a file list or reader - abort." );
                                evt.rejectDrop();
                            }
                        }
                    }
                    catch (IOException io) {
                    	log( out, "FileDrop: IOException - abort:" );
                        io.printStackTrace( out );
                        evt.rejectDrop();
                    }
                    catch (UnsupportedFlavorException ufe) {
                    	log( out, "FileDrop: UnsupportedFlavorException - abort:" );
                        ufe.printStackTrace( out );
                        evt.rejectDrop();
                    }
                    finally { 
                        if( c instanceof JComponent ) {   
                        	JComponent jc = (JComponent) c;
                            jc.setBorder( normalBorder );
                            log( out, "FileDrop: normal border restored." );
                        }
                    }
                }

                public void dragExit( DropTargetEvent evt ) {
                	log( out, "FileDrop: dragExit event." );
                    if( c instanceof JComponent ) {
                    	JComponent jc = (JComponent) c;
                        jc.setBorder( normalBorder );
                        log( out, "FileDrop: normal border restored." );
                    }
                }

                public void dropActionChanged( DropTargetDragEvent evt ) {
                	log( out, "FileDrop: dropActionChanged event." );
                    if( isDragOk( out, evt ) ) {
                        evt.acceptDrag( DnDConstants.ACTION_COPY );
                        log( out, "FileDrop: event accepted." );
                    }
                    else {
                    	evt.rejectDrag();
                        log( out, "FileDrop: event rejected." );
                    }
                }
            };

            makeDropTarget( out, c, recursive );
        }
        else { 
        	log( out, "FileDrop: Drag and drop is not supported with this JVM" );
        }
    }
    
    
    private static boolean supportsDnD() {
        if( supportsDnD == null ) {   
            boolean support = false;
            try {
            	Class arbitraryDndClass = Class.forName( "java.awt.dnd.DnDConstants" );
                support = true;
            }
            catch( Exception e ) {
            	support = false;
            }
            supportsDnD = new Boolean( support );
        }
        
        return supportsDnD.booleanValue();
    }
    
     private static String ZERO_CHAR_STRING = "" + (char)0;
     private static File[] createFileArray(BufferedReader bReader, PrintStream out) {
        try { 
            List list = new ArrayList();
            String line = null;
            while ((line = bReader.readLine()) != null) {
                try {
                    if(ZERO_CHAR_STRING.equals(line)) continue; 
                    
                    File file = new File(new URI(line));
                    list.add(file);
                } catch (Exception ex) {
                    log(out, "Error with " + line + ": " + ex.getMessage());
                }
            }

            return (File[]) list.toArray(new File[list.size()]);
        } 
        catch (IOException ex) {
            log(out, "FileDrop: IOException");
        }
        return new File[0];
      }
    
    private void makeDropTarget( final PrintStream out, final Component c, boolean recursive ) {
        final DropTarget dt = new DropTarget();
        try {
        	dt.addDropTargetListener( dropListener );
        }
        catch( TooManyListenersException e ) {
        	e.printStackTrace();
            log(out, "FileDrop: Drop will not work due to previous error. Do you have another listener attached?" );
        }

        c.addHierarchyListener( new HierarchyListener() {
           public void hierarchyChanged( HierarchyEvent evt ) {
              log( out, "FileDrop: Hierarchy changed." );
                Component parent = c.getParent();
                if( parent == null ) {   
                	c.setDropTarget( null );
                    log( out, "FileDrop: Drop target cleared from component." );
                }
                else {   
                	new DropTarget(c, dropListener);
                    log( out, "FileDrop: Drop target added to component." );
                }
            }
        });
        
        if( c.getParent() != null ) {
            new DropTarget(c, dropListener);
        }
        
        if( recursive && (c instanceof Container ) ) {
        	Container cont = (java.awt.Container) c;
            Component[] comps = cont.getComponents();

            for( int i = 0; i < comps.length; i++ ) {
                makeDropTarget( out, comps[i], recursive );
            }
        }
    } 

    private boolean isDragOk( final PrintStream out, final DropTargetDragEvent evt ) {
    	boolean ok = false;
    	DataFlavor[] flavors = evt.getCurrentDataFlavors();

        int i = 0;
        while( !ok && i < flavors.length ) {
            final DataFlavor curFlavor = flavors[i];
            if( curFlavor.equals( java.awt.datatransfer.DataFlavor.javaFileListFlavor ) || curFlavor.isRepresentationClassReader()) {
                ok = true;
            }

            i++;
        }

        if( out != null ) {
        	if( flavors.length == 0 ) {
                log( out, "FileDrop: no data flavors." );
        	}
            for( i = 0; i < flavors.length; i++ ) {
                log( out, flavors[i].toString() );
            }
        }
        
        return ok;
    }

    private static void log( PrintStream out, String message ) {
        if( out != null ) {
            out.println( message );
        }
    } 
    
    public static boolean remove( Component c) { return remove( null, c, true ); }

    public static boolean remove( PrintStream out, Component c, boolean recursive ) {
        if( supportsDnD() ) {
        	log( out, "FileDrop: Removing drag-and-drop hooks." );
            c.setDropTarget( null );
            if( recursive && ( c instanceof Container ) ) {   
            	Component[] comps = ((Container)c).getComponents();
                for( int i = 0; i < comps.length; i++ ) {
                    remove( out, comps[i], recursive );
                }
                return true;
            }
            else return false;
        }
        else return false;
    }
    
    public static interface Listener {
        public abstract void filesDropped( File[] files );
    }
    
    public static class Event extends EventObject {
        private File[] files;

        public Event( File[] files, Object source ) {
            super( source );
            this.files = files;
        }
        
        public File[] getFiles() { return files; }
    }
    
    public static class TransferableObject implements Transferable {
        public final static String MIME_TYPE = "application/x-net.iharder.dnd.TransferableObject";
        public final static DataFlavor DATA_FLAVOR = new DataFlavor( FileDrop.TransferableObject.class, MIME_TYPE );
        private Fetcher fetcher;
        private Object data;

        private java.awt.datatransfer.DataFlavor customFlavor; 

        public TransferableObject( Object data ) {
        	this.data = data;
            this.customFlavor = new DataFlavor( data.getClass(), MIME_TYPE );
        }  
        
        public TransferableObject( Fetcher fetcher ) { this.fetcher = fetcher; }
        
        public TransferableObject( Class dataClass, Fetcher fetcher ) { 
        	this.fetcher = fetcher;
            this.customFlavor = new DataFlavor( dataClass, MIME_TYPE );
        }
        
        public DataFlavor getCustomDataFlavor() { return customFlavor; }
        
        public DataFlavor[] getTransferDataFlavors() 
        {   
            if( customFlavor != null ) {
                return new DataFlavor[] {   
            		customFlavor, DATA_FLAVOR, DataFlavor.stringFlavor
                };
        	}
            else {
                return new java.awt.datatransfer.DataFlavor[] {
            		DATA_FLAVOR, DataFlavor.stringFlavor
                };
            }
        }

        public Object getTransferData( DataFlavor flavor ) throws UnsupportedFlavorException, IOException {
            if( flavor.equals( DATA_FLAVOR ) ) {
                return fetcher == null ? data : fetcher.getObject();
        	}
            if( flavor.equals( DataFlavor.stringFlavor ) ) {
                return fetcher == null ? data.toString() : fetcher.getObject().toString();
            }
            
            throw new UnsupportedFlavorException(flavor);
        }
        
        public boolean isDataFlavorSupported( java.awt.datatransfer.DataFlavor flavor ) {
            if( flavor.equals( DATA_FLAVOR ) ) { return true;}
            if( flavor.equals( DataFlavor.stringFlavor ) ) { return true;}
            
            return false;
        }

        public static interface Fetcher {
            public abstract Object getObject();
        }
    }
}
