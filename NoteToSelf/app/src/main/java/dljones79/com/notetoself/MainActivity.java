package dljones79.com.notetoself;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteAdapter mNoteAdapter;

    // region overridden methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogNewNote dialog = new DialogNewNote();
                dialog.show(getSupportFragmentManager(), "");
            }
        });

        /*
        1. Initialize mNoteAdapter
        2. Get a reference to ListView
        3. Bind them together
         */
        mNoteAdapter = new NoteAdapter();
        ListView listNote = (ListView) findViewById(R.id.listView);
        listNote.setAdapter(mNoteAdapter);

        // Handle clicks on the ListView
        listNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int                 whichItem, long id) {

                /*
                  Create  a temporary Note
                  Which is a reference to the Note
                  that has just been clicked
                */
                Note tempNote = mNoteAdapter.getItem(whichItem);

                // Create a new dialog window
                DialogShowNote dialog = new DialogShowNote();
                // Send in a reference to the note to be shown
                dialog.sendNoteSelected(tempNote);

                // Show the dialog window with the note in it
                dialog.show(getSupportFragmentManager(), "");

            }
        });

    } // end of onCreate()

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    } // end of onCreateOptionsMenu()

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    } // end of onOptionsItemSelected()
    // endregion overridden methods

    // region class methods

    /*
    When DialogNewNote calls this method, it will pass it straight to
    the addNote method in the NoteAdapter class, and will be added to
    ArrayList (noteList). The adapter will be notified of the change as
    well, which will then trigger the BaseAdapter class to do its work and
    keep the view up-to-date.
     */
    public void createNewNote(Note n) {
        mNoteAdapter.addNote(n);
    } // end of createNewNote()
    // end region class methods

    //region inner classes
    /*
    Here we create an inner class called NoteAdapter that extends
    BaseAdapter. This class holds an ArrayList called noteList and the
    getItem method returns a Note object.
     */
    public class NoteAdapter extends BaseAdapter {

        // Declare and initialize a List that will hold notes
        List<Note> noteList = new ArrayList<Note>();

        // Get the number of notes in the list (ArrayList)
        @Override
        public int getCount() {
            return noteList.size();
        }

        // Get an item at a particular index (whichItem) in the array
        @Override
        public Note getItem(int whichItem) {
            return noteList.get(whichItem);
        }

        // Get the id of an item in the array
        @Override
        public long getItemId(int whichItem) {
            return whichItem;
        }

        /*
        The view object reference is an instance of the list item that is
        necessary to be displayed as evaluated by BaseAdapter, and whichItem is
        the position in the ArrayList of the Note object that needs to be
        displayed in it.
         */
        @Override
        public View getView(int whichItem, View view, ViewGroup viewGroup)   {

            // Implement this method next
            // Has view been inflated already
            if(view == null){

                // If not, do so here
                // First create a LayoutInflater
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                // Now instantiate view using inflater.inflate
                // using the listitem layout
                view = inflater.inflate(R.layout.listitem, viewGroup,false);
                // The false parameter is neccessary
                // because of the way that we want to use listitem

            }// End if

            // Grab a reference to all our TextView and ImageView widgets
            TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            TextView txtDescription = (TextView) view.findViewById(R.id.txtDescription);
            ImageView ivImportant = (ImageView) view.findViewById(R.id.imageViewImportant);
            ImageView ivTodo = (ImageView) view.findViewById(R.id.imageViewTodo);
            ImageView ivIdea = (ImageView) view.findViewById(R.id.imageViewIdea);

            // Hide any ImageView widgets that are not relevant
            Note tempNote = noteList.get(whichItem);

            if (!tempNote.isImportant()){
                ivImportant.setVisibility(View.GONE);
            }

            if (!tempNote.isTodo()){
                ivTodo.setVisibility(View.GONE);
            }

            if (!tempNote.isIdea()){
                ivIdea.setVisibility(View.GONE);
            }

            // Add the text to the heading and description
            txtTitle.setText(tempNote.getTitle());
            txtDescription.setText(tempNote.getDescription());

            return view;
        } // end getView()

        /*
        This method adds a new note to our array list.
        notifyDataSetChanged() will tell NoteAdapter that the data in
        noteList has changed and that the ListView might need to be
        updated.
         */
        public void addNote(Note n){
            noteList.add(n);
            notifyDataSetChanged();
        }

    } // end NoteAdapter.class
    // endregion inner classes

} // end of MainActivity.class
